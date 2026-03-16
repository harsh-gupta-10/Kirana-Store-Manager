package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.remote.ai.GeminiActionDetector
import com.kiranstore.manager.data.remote.ai.VoiceAction
import com.kiranstore.manager.data.remote.ai.VoiceCommandResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VoiceUiState(
    val transcript: String = "",
    val isListening: Boolean = false,
    val isProcessing: Boolean = false,
    val result: VoiceCommandResult? = null,
    val error: String? = null
)

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val detector: GeminiActionDetector
) : ViewModel() {

    private val _state = MutableStateFlow(VoiceUiState())
    val state: StateFlow<VoiceUiState> = _state

    fun setListening(listening: Boolean) {
        _state.update { it.copy(isListening = listening) }
    }

    fun submitTranscript(transcript: String) {
        if (transcript.isBlank()) return
        _state.update {
            it.copy(
                transcript = transcript,
                isProcessing = true,
                error = null,
                result = null
            )
        }
        viewModelScope.launch {
            val result = detector.detectAction(transcript)
            _state.update {
                it.copy(
                    isProcessing = false,
                    result = result,
                    error = if (result.action == VoiceAction.UNKNOWN) result.reason else null
                )
            }
        }
    }

    fun clearResult() {
        _state.update { it.copy(result = null, error = null, isProcessing = false) }
    }
}
