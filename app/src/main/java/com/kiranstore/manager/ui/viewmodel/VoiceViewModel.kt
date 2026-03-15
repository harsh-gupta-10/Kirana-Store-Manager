package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.data.repository.*
import com.kiranstore.manager.services.ai.GeminiService
import com.kiranstore.manager.services.ai.VoiceAction
import com.kiranstore.manager.services.speech.SpeechRecognitionService
import com.kiranstore.manager.services.speech.SpeechState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VoiceUiState(
    val isListening: Boolean = false,
    val recognizedText: String = "",
    val actionResult: String = "",
    val error: String = ""
)

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val speechService: SpeechRecognitionService,
    private val geminiService: GeminiService,
    private val debtRepo: DebtRepository,
    private val customerRepo: CustomerRepository,
    private val buyListRepo: BuyListRepository,
    private val taskRepo: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(VoiceUiState())
    val state: StateFlow<VoiceUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            speechService.state.collect { speechState ->
                when (speechState) {
                    is SpeechState.Listening -> _state.update { it.copy(isListening = true, recognizedText = "") }
                    is SpeechState.Result    -> {
                        _state.update { it.copy(isListening = false, recognizedText = speechState.text) }
                        processVoiceText(speechState.text)
                    }
                    is SpeechState.Error     -> _state.update { it.copy(isListening = false, error = speechState.message) }
                    is SpeechState.Idle      -> _state.update { it.copy(isListening = false) }
                }
            }
        }
    }

    fun startListening() = speechService.startListening()
    fun stopListening()  = speechService.stopListening()
    fun reset()          { speechService.reset(); _state.value = VoiceUiState() }

    private fun processVoiceText(text: String) {
        viewModelScope.launch {
            try {
                val action = geminiService.parseVoiceCommand(text)
                handleAction(action)
            } catch (e: Exception) {
                _state.update { it.copy(error = "Could not process command: ${e.message}") }
            }
        }
    }

    private suspend fun handleAction(action: VoiceAction, shopId: Long = 1L) {
        when (action.action) {
            "add_debt" -> {
                val customer = getOrCreateCustomer(shopId, action.customerName)
                debtRepo.addDebt(DebtEntity(
                    customerId = customer.id, shopId = shopId,
                    itemName = action.itemName.ifBlank { "Items" },
                    amount = action.amount
                ))
                _state.update { it.copy(actionResult = "✅ ₹${action.amount.toInt()} udhaar added for ${action.customerName}") }
            }
            "add_payment" -> {
                val customer = customerRepo.findByName(shopId, action.customerName)
                if (customer != null) {
                    debtRepo.addPayment(DebtPaymentEntity(customerId = customer.id, shopId = shopId, amount = action.amount))
                    _state.update { it.copy(actionResult = "✅ ₹${action.amount.toInt()} payment recorded for ${action.customerName}") }
                } else {
                    _state.update { it.copy(error = "Customer '${action.customerName}' not found") }
                }
            }
            "add_buy_item" -> {
                buyListRepo.addItem(BuyListItemEntity(shopId = shopId, name = action.itemName, quantity = action.quantity, priority = action.priority))
                _state.update { it.copy(actionResult = "✅ ${action.itemName} added to buy list") }
            }
            "add_task" -> {
                taskRepo.addTask(TaskEntity(shopId = shopId, name = action.taskName, notes = action.notes))
                _state.update { it.copy(actionResult = "✅ Task '${action.taskName}' added") }
            }
            else -> _state.update { it.copy(error = "Could not understand command. Please try again.") }
        }
    }

    private suspend fun getOrCreateCustomer(shopId: Long, name: String): CustomerEntity {
        return customerRepo.findByName(shopId, name)
            ?: CustomerEntity(shopId = shopId, name = name, phone = "").also {
                val id = customerRepo.addCustomer(it)
                return it.copy(id = id)
            }
    }
}
