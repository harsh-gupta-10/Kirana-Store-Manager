package com.kiranstore.manager.data.remote.ai

enum class VoiceAction {
    ADD_CUSTOMER,
    ADD_UDHAAR,
    ADD_PAYMENT,
    ADD_RENTAL,
    SHOW_SUMMARY,
    UNKNOWN
}

data class VoiceCommandResult(
    val action: VoiceAction,
    val transcript: String,
    val customerName: String? = null,
    val amount: Double? = null,
    val notes: String? = null,
    val rawResponse: String = "",
    val reason: String? = null
)
