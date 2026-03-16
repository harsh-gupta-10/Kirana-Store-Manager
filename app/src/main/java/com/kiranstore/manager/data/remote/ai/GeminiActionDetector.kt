package com.kiranstore.manager.data.remote.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.TextPart
import com.kiranstore.manager.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Wraps Gemini to classify a spoken command into an actionable intent for the store app.
 */
@Singleton
class GeminiActionDetector @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val model: GenerativeModel? = BuildConfig.GEMINI_API_KEY
        .takeIf { it.isNotBlank() }
        ?.let { apiKey ->
            GenerativeModel(
                modelName = "gemini-pro",
                apiKey = apiKey
            )
        }

    suspend fun detectAction(transcript: String): VoiceCommandResult = withContext(dispatcher) {
        if (transcript.isBlank()) {
            return@withContext VoiceCommandResult(
                action = VoiceAction.UNKNOWN,
                transcript = transcript,
                reason = "Empty command"
            )
        }
        val activeModel = model ?: return@withContext VoiceCommandResult(
            action = VoiceAction.UNKNOWN,
            transcript = transcript,
            reason = "Gemini API key missing"
        )

        val prompt = """
            You are an assistant for a small Indian Kirana store app. Convert the spoken command into a JSON object.
            Allowed actions: ADD_CUSTOMER, ADD_UDHAAR, ADD_PAYMENT, ADD_RENTAL, SHOW_SUMMARY.
            Fields:
              - action: one of the allowed actions.
              - customerName: string or null.
              - amount: number or null (₹).
              - notes: short string in English.
            Only return JSON, no extra text. Example:
            {"action":"ADD_UDHAAR","customerName":"Ravi","amount":120.5,"notes":"add udhaar for groceries"}
            Command: "$transcript"
        """.trimIndent()

        val response = runCatching { activeModel.generateContent(prompt) }.getOrElse { error ->
            return@withContext VoiceCommandResult(
                action = VoiceAction.UNKNOWN,
                transcript = transcript,
                reason = error.message
            )
        }

        val rawText = extractText(response)
        return@withContext parseResult(rawText.ifBlank { response.toString() }, transcript)
    }

    private fun extractText(response: GenerateContentResponse): String {
        val firstPart = response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
        return when (firstPart) {
            is TextPart -> firstPart.text
            else -> ""
        } ?: ""
    }

    private fun parseResult(raw: String, transcript: String): VoiceCommandResult {
        val cleaned = raw.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        return try {
            val obj = JSONObject(cleaned)
            val action = VoiceAction.values()
                .firstOrNull { it.name == obj.optString("action", "").uppercase() }
                ?: VoiceAction.UNKNOWN
            VoiceCommandResult(
                action = action,
                transcript = transcript,
                customerName = obj.optString("customerName").takeIf { it.isNotBlank() },
                amount = obj.optDouble("amount").takeIf { !obj.isNull("amount") },
                notes = obj.optString("notes").takeIf { it.isNotBlank() },
                rawResponse = raw,
                reason = if (action == VoiceAction.UNKNOWN) "No actionable intent" else null
            )
        } catch (ex: Exception) {
            VoiceCommandResult(
                action = VoiceAction.UNKNOWN,
                transcript = transcript,
                rawResponse = raw,
                reason = ex.message
            )
        }
    }
}
