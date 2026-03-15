package com.kiranstore.manager.services.ai

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.kiranstore.manager.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────
// PARSED VOICE ACTION DATA CLASSES
// ─────────────────────────────────────────────
data class VoiceAction(
    val action: String = "",                    // add_debt | add_payment | add_buy_item | add_task | add_rental | unknown
    @SerializedName("customer_name") val customerName: String = "",
    val amount: Double = 0.0,
    @SerializedName("item_name") val itemName: String = "",
    val quantity: String = "",
    @SerializedName("task_name") val taskName: String = "",
    @SerializedName("machine_name") val machineName: String = "",
    val notes: String = "",
    val priority: String = "MEDIUM"
)

// ─────────────────────────────────────────────
// GEMINI SERVICE
// ─────────────────────────────────────────────
@Singleton
class GeminiService @Inject constructor() {

    private val TAG = "GeminiService"
    private val gson = Gson()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val apiKey = BuildConfig.GEMINI_API_KEY
    private val endpoint =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

    // ── System prompt for shop command parsing ─────────────
    private val systemPrompt = """
        You are a voice command parser for an Indian shop management app called "Kiran Store Manager".
        The shopkeeper speaks in Hindi, Hinglish, or English.
        
        Parse the voice command and return ONLY a valid JSON object (no markdown, no explanation).
        
        Possible actions and their JSON format:
        
        1. add_debt (customer bought on credit):
        {"action":"add_debt","customer_name":"Ramesh","item_name":"Sugar","amount":200}
        
        2. add_payment (customer paid back):
        {"action":"add_payment","customer_name":"Ramesh","amount":100}
        
        3. add_buy_item (add item to shop's buy list):
        {"action":"add_buy_item","item_name":"Milk","quantity":"20 packets","priority":"HIGH"}
        
        4. add_task (add daily task):
        {"action":"add_task","task_name":"Call supplier","notes":""}
        
        5. add_rental (rent a machine):
        {"action":"add_rental","customer_name":"Suresh","machine_name":"Drill Machine","amount":200}
        
        6. unknown (cannot parse):
        {"action":"unknown"}
        
        Examples:
        - "Ramesh ko 200 ka udhaar likh do" → {"action":"add_debt","customer_name":"Ramesh","item_name":"","amount":200}
        - "Raju ne 100 rupaye diye" → {"action":"add_payment","customer_name":"Raju","amount":100}
        - "Buy list me 20 packet milk add karo" → {"action":"add_buy_item","item_name":"Milk","quantity":"20 packets","priority":"MEDIUM"}
        - "Aaj ka kaam: supplier ko call karo" → {"action":"add_task","task_name":"Call supplier","notes":""}
        
        Return ONLY valid JSON. No extra text.
    """.trimIndent()

    // ── Main parse function ────────────────────────────────
    suspend fun parseVoiceCommand(voiceText: String): VoiceAction {
        if (apiKey.isBlank()) {
            Log.w(TAG, "Gemini API key not set — returning unknown action")
            return VoiceAction(action = "unknown")
        }
        return try {
            val requestBody = buildGeminiRequest(voiceText)
            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody.toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: return VoiceAction(action = "unknown")

            val text = extractTextFromGeminiResponse(responseBody)
            val cleanJson = text.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            gson.fromJson(cleanJson, VoiceAction::class.java) ?: VoiceAction(action = "unknown")
        } catch (e: Exception) {
            Log.e(TAG, "Gemini parse error: ${e.message}")
            VoiceAction(action = "unknown")
        }
    }

    // ── Build Gemini API request JSON ──────────────────────
    private fun buildGeminiRequest(userText: String): String {
        return """
            {
              "contents": [
                {
                  "parts": [
                    { "text": "$systemPrompt\n\nVoice Input: $userText" }
                  ]
                }
              ],
              "generationConfig": {
                "temperature": 0.1,
                "maxOutputTokens": 256
              }
            }
        """.trimIndent()
    }

    // ── Extract text from Gemini response ─────────────────
    private fun extractTextFromGeminiResponse(json: String): String {
        return try {
            val obj = JSONObject(json)
            obj.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
        } catch (e: Exception) {
            Log.e(TAG, "Parse response error: ${e.message}")
            "{\"action\":\"unknown\"}"
        }
    }
}
