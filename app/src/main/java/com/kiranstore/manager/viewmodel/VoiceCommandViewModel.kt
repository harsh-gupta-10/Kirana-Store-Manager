package com.kiranstore.manager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.remote.GeminiManager
import com.kiranstore.manager.data.remote.VoiceAction
import com.kiranstore.manager.voice.VoiceCommandManager
import com.kiranstore.manager.voice.VoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VoiceCommandState(
    val isListening: Boolean = false,
    val recognizedText: String = "",
    val parsedAction: VoiceAction? = null,
    val error: String? = null,
    val isProcessing: Boolean = false
)

@HiltViewModel
class VoiceCommandViewModel @Inject constructor(
    application: Application,
    private val geminiManager: GeminiManager
) : AndroidViewModel(application) {

    private val voiceCommandManager = VoiceCommandManager(application)

    private val _state = MutableStateFlow(VoiceCommandState())
    val state: StateFlow<VoiceCommandState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            voiceCommandManager.voiceState.collect { voiceState ->
                when (voiceState) {
                    is VoiceState.Idle -> {
                        _state.value = _state.value.copy(isListening = false)
                    }
                    is VoiceState.Listening -> {
                        _state.value = _state.value.copy(
                            isListening = true,
                            error = null,
                            parsedAction = null,
                            recognizedText = ""
                        )
                    }
                    is VoiceState.Result -> {
                        _state.value = _state.value.copy(
                            isListening = false,
                            recognizedText = voiceState.text,
                            isProcessing = true
                        )
                        processVoiceResult(voiceState.text)
                    }
                    is VoiceState.Error -> {
                        _state.value = _state.value.copy(
                            isListening = false,
                            error = voiceState.message,
                            isProcessing = false
                        )
                    }
                }
            }
        }
    }

    fun startListening() {
        _state.value = VoiceCommandState(isListening = true)
        voiceCommandManager.startListening()
    }

    fun stopListening() {
        voiceCommandManager.stopListening()
        _state.value = _state.value.copy(isListening = false)
    }

    private fun processVoiceResult(text: String) {
        viewModelScope.launch {
            try {
                val action = geminiManager.parseVoiceCommand(text)
                _state.value = _state.value.copy(
                    parsedAction = action,
                    isProcessing = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to parse command",
                    isProcessing = false
                )
            }
        }
    }

    fun clearState() {
        voiceCommandManager.resetState()
        _state.value = VoiceCommandState()
    }

    override fun onCleared() {
        super.onCleared()
        voiceCommandManager.destroy()
    }
}
