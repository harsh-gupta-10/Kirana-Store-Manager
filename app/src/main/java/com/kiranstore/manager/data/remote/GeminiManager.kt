package com.kiranstore.manager.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.kiranstore.manager.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

sealed class VoiceAction {
    data class AddUdhaar(val customerName: String?, val amount: Double?, val itemName: String?) : VoiceAction()
    data class AddPayment(val customerName: String?, val amount: Double?) : VoiceAction()
    data class AddCustomer(val name: String?, val phone: String?) : VoiceAction()
    data class AddRental(val customerName: String?, val machineName: String?) : VoiceAction()
    data class AddTask(val title: String?) : VoiceAction()
    data class AddBuyItem(val itemName: String?, val quantity: String?) : VoiceAction()
    object NavigateHome : VoiceAction()
    object NavigateCustomers : VoiceAction()
    object NavigateRentals : VoiceAction()
    object NavigateSettings : VoiceAction()
    data class Unknown(val rawText: String) : VoiceAction()
}

@Singleton
class GeminiManager @Inject constructor() {

    private val model: GenerativeModel? by lazy {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) null
        else GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )
    }

    suspend fun parseVoiceCommand(spokenText: String): VoiceAction {
        val geminiModel = model ?: return parseLocally(spokenText)

        return try {
            val prompt = buildPrompt(spokenText)
            val response = geminiModel.generateContent(prompt)
            val resultText = response.text ?: return parseLocally(spokenText)
            parseGeminiResponse(resultText, spokenText)
        } catch (e: Exception) {
            parseLocally(spokenText)
        }
    }

    private fun buildPrompt(spokenText: String): String {
        return """
            You are a voice command parser for a store management app called "Kiran General Store Manager".
            The app manages customers, debts (udhaar), payments, machine rentals, tasks, and a buy list.
            
            Parse the following voice command and respond with EXACTLY one line in this format:
            ACTION|param1=value1|param2=value2
            
            Available actions:
            - ADD_UDHAAR|customer=name|amount=number|item=name
            - ADD_PAYMENT|customer=name|amount=number
            - ADD_CUSTOMER|name=name|phone=number
            - ADD_RENTAL|customer=name|machine=name
            - ADD_TASK|title=text
            - ADD_BUY_ITEM|item=name|quantity=amount
            - NAVIGATE|screen=home/customers/rentals/settings
            - UNKNOWN|raw=original text
            
            Voice command: "$spokenText"
            
            Respond with only the parsed action line, nothing else.
        """.trimIndent()
    }

    private fun parseGeminiResponse(response: String, originalText: String): VoiceAction {
        val line = response.trim().lines().firstOrNull()?.trim() ?: return VoiceAction.Unknown(originalText)
        val parts = line.split("|")
        val action = parts.firstOrNull() ?: return VoiceAction.Unknown(originalText)
        val params = parts.drop(1).mapNotNull { param ->
            val eqIdx = param.indexOf('=')
            if (eqIdx > 0) {
                param.substring(0, eqIdx).trim() to param.substring(eqIdx + 1).trim()
            } else {
                null
            }
        }.toMap()

        return when (action.uppercase()) {
            "ADD_UDHAAR" -> VoiceAction.AddUdhaar(
                customerName = params["customer"]?.takeIf { it.isNotBlank() },
                amount = params["amount"]?.toDoubleOrNull(),
                itemName = params["item"]?.takeIf { it.isNotBlank() }
            )
            "ADD_PAYMENT" -> VoiceAction.AddPayment(
                customerName = params["customer"]?.takeIf { it.isNotBlank() },
                amount = params["amount"]?.toDoubleOrNull()
            )
            "ADD_CUSTOMER" -> VoiceAction.AddCustomer(
                name = params["name"]?.takeIf { it.isNotBlank() },
                phone = params["phone"]?.takeIf { it.isNotBlank() }
            )
            "ADD_RENTAL" -> VoiceAction.AddRental(
                customerName = params["customer"]?.takeIf { it.isNotBlank() },
                machineName = params["machine"]?.takeIf { it.isNotBlank() }
            )
            "ADD_TASK" -> VoiceAction.AddTask(
                title = params["title"]?.takeIf { it.isNotBlank() }
            )
            "ADD_BUY_ITEM" -> VoiceAction.AddBuyItem(
                itemName = params["item"]?.takeIf { it.isNotBlank() },
                quantity = params["quantity"]?.takeIf { it.isNotBlank() }
            )
            "NAVIGATE" -> when (params["screen"]?.lowercase()) {
                "home" -> VoiceAction.NavigateHome
                "customers" -> VoiceAction.NavigateCustomers
                "rentals" -> VoiceAction.NavigateRentals
                "settings" -> VoiceAction.NavigateSettings
                else -> VoiceAction.Unknown(originalText)
            }
            else -> VoiceAction.Unknown(originalText)
        }
    }

    internal fun parseLocally(spokenText: String): VoiceAction {
        val text = spokenText.lowercase().trim()

        return when {
            // Udhaar / debt patterns
            text.contains("udhaar") || text.contains("udhar") ||
            text.contains("credit") || text.contains("debt") -> {
                val amount = extractAmount(text)
                val name = extractNameAfterKeyword(text, listOf("for", "of", "from", "ka", "ki"))
                VoiceAction.AddUdhaar(
                    customerName = name,
                    amount = amount,
                    itemName = extractItemName(text)
                )
            }

            // Payment patterns
            text.contains("payment") || text.contains("paid") ||
            text.contains("received") || text.contains("bhugtan") -> {
                val amount = extractAmount(text)
                val name = extractNameAfterKeyword(text, listOf("from", "by", "se"))
                VoiceAction.AddPayment(customerName = name, amount = amount)
            }

            // Customer patterns
            text.contains("add customer") || text.contains("new customer") ||
            text.contains("naya customer") || text.contains("naya grahak") -> {
                val name = extractNameAfterKeyword(text, listOf("named", "name", "called", "naam"))
                VoiceAction.AddCustomer(name = name, phone = null)
            }

            // Rental patterns
            text.contains("rent") || text.contains("kiraya") || text.contains("machine") -> {
                val name = extractNameAfterKeyword(text, listOf("to", "for", "ko"))
                VoiceAction.AddRental(customerName = name, machineName = null)
            }

            // Task patterns
            text.contains("task") || text.contains("kaam") || text.contains("todo") -> {
                val title = text.replace(Regex("(add|new|create|naya|kaam|task|todo)"), "").trim()
                VoiceAction.AddTask(title = title.takeIf { it.isNotBlank() })
            }

            // Buy list patterns
            text.contains("buy") || text.contains("kharid") || text.contains("purchase") -> {
                val item = text.replace(Regex("(add|buy|kharid|purchase|item|to list)"), "").trim()
                VoiceAction.AddBuyItem(itemName = item.takeIf { it.isNotBlank() }, quantity = null)
            }

            // Navigation patterns
            text.contains("go to home") || text.contains("open home") -> VoiceAction.NavigateHome
            text.contains("go to customer") || text.contains("open customer") -> VoiceAction.NavigateCustomers
            text.contains("go to rental") || text.contains("open rental") -> VoiceAction.NavigateRentals
            text.contains("go to setting") || text.contains("open setting") -> VoiceAction.NavigateSettings

            else -> VoiceAction.Unknown(spokenText)
        }
    }

    private fun extractAmount(text: String): Double? {
        val patterns = listOf(
            Regex("(\\d+\\.?\\d*)\\s*(rupee|rs|₹|rupay)"),
            Regex("(rupee|rs|₹|rupay)\\s*(\\d+\\.?\\d*)"),
            Regex("(\\d+\\.?\\d*)")
        )
        for (pattern in patterns) {
            val match = pattern.find(text)
            if (match != null) {
                val groups = match.groupValues
                val numStr = groups.firstOrNull { it.toDoubleOrNull() != null }
                if (numStr != null) return numStr.toDouble()
            }
        }
        return null
    }

    private fun extractNameAfterKeyword(text: String, keywords: List<String>): String? {
        for (keyword in keywords) {
            val idx = text.indexOf(keyword)
            if (idx >= 0) {
                val afterKeyword = text.substring(idx + keyword.length).trim()
                val name = afterKeyword.split(Regex("\\s+(\\d|rupee|rs|₹|amount|for|item)"))[0].trim()
                if (name.isNotBlank()) return name
            }
        }
        return null
    }

    private fun extractItemName(text: String): String? {
        val keywords = listOf("for item", "item", "saman", "cheez")
        for (keyword in keywords) {
            val idx = text.indexOf(keyword)
            if (idx >= 0) {
                val afterKeyword = text.substring(idx + keyword.length).trim()
                val item = afterKeyword.split(Regex("\\s+(\\d|rupee|rs|₹|amount|for)"))[0].trim()
                if (item.isNotBlank()) return item
            }
        }
        return null
    }
}
