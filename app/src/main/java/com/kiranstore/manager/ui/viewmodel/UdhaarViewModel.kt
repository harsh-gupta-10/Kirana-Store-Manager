package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerWithBalance(
    val customer: CustomerEntity,
    val balance: Double,
    val lastEntryTime: Long = 0L
)

data class UdhaarUiState(
    val shopId: Long = 0L,
    val customersWithBalance: List<CustomerWithBalance> = emptyList(),
    val totalOutstanding: Double = 0.0,
    val recoveredToday: Double = 0.0,
    val searchQuery: String = "",
    val isAddDialogOpen: Boolean = false,
    val isPaymentDialogOpen: Boolean = false,
    val selectedCustomerId: Long? = null,
    val error: String = ""
)

@HiltViewModel
class UdhaarViewModel @Inject constructor(
    private val debtRepo: DebtRepository,
    private val customerRepo: CustomerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UdhaarUiState())
    val state: StateFlow<UdhaarUiState> = _state.asStateFlow()

    fun init(shopId: Long) {
        _state.update { it.copy(shopId = shopId) }
        viewModelScope.launch {
            customerRepo.getAllCustomers(shopId).collect { customers ->
                val withBalances = customers.map { c ->
                    val bal = debtRepo.getCustomerBalance(c.id).firstOrNull() ?: 0.0
                    CustomerWithBalance(c, bal)
                }
                _state.update { it.copy(customersWithBalance = withBalances) }
            }
        }
        viewModelScope.launch {
            debtRepo.getTotalOutstanding(shopId).collect { total ->
                _state.update { it.copy(totalOutstanding = total ?: 0.0) }
            }
        }
    }

    fun search(query: String) = _state.update { it.copy(searchQuery = query) }

    fun addDebt(customerId: Long, itemName: String, amount: Double, notes: String) {
        val shopId = _state.value.shopId
        viewModelScope.launch {
            debtRepo.addDebt(DebtEntity(customerId = customerId, shopId = shopId, itemName = itemName, amount = amount, notes = notes))
        }
    }

    fun addPayment(customerId: Long, amount: Double) {
        val shopId = _state.value.shopId
        viewModelScope.launch {
            debtRepo.addPayment(DebtPaymentEntity(customerId = customerId, shopId = shopId, amount = amount))
        }
    }

    fun markAsPaid(debtId: Long) = viewModelScope.launch { debtRepo.markAsPaid(debtId) }

    fun openAddDialog(customerId: Long? = null) = _state.update { it.copy(isAddDialogOpen = true, selectedCustomerId = customerId) }
    fun closeAddDialog() = _state.update { it.copy(isAddDialogOpen = false) }
    fun openPaymentDialog(customerId: Long) = _state.update { it.copy(isPaymentDialogOpen = true, selectedCustomerId = customerId) }
    fun closePaymentDialog() = _state.update { it.copy(isPaymentDialogOpen = false) }

    val filteredCustomers: List<CustomerWithBalance>
        get() {
            val q = _state.value.searchQuery
            return if (q.isBlank()) _state.value.customersWithBalance
            else _state.value.customersWithBalance.filter {
                it.customer.name.contains(q, ignoreCase = true) || it.customer.phone.contains(q)
            }
        }
}

// ─────────────────────────────────────────────────────────
// UDHAAR DETAIL VIEW MODEL
// ─────────────────────────────────────────────────────────
data class UdhaarDetailState(
    val customer: CustomerEntity? = null,
    val debts: List<DebtEntity> = emptyList(),
    val payments: List<DebtPaymentEntity> = emptyList(),
    val balance: Double = 0.0
)

@HiltViewModel
class UdhaarDetailViewModel @Inject constructor(
    private val debtRepo: DebtRepository,
    private val customerRepo: CustomerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UdhaarDetailState())
    val state: StateFlow<UdhaarDetailState> = _state.asStateFlow()

    fun init(customerId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(customer = customerRepo.getById(customerId)) }
            launch { debtRepo.getDebtsByCustomer(customerId).collect { debts -> _state.update { it.copy(debts = debts) } } }
            launch { debtRepo.getPaymentsByCustomer(customerId).collect { payments -> _state.update { it.copy(payments = payments) } } }
            launch { debtRepo.getCustomerBalance(customerId).collect { bal -> _state.update { it.copy(balance = bal ?: 0.0) } } }
        }
    }

    fun markAsPaid(debtId: Long) = viewModelScope.launch { debtRepo.markAsPaid(debtId) }

    fun addPayment(customerId: Long, shopId: Long, amount: Double) = viewModelScope.launch {
        debtRepo.addPayment(DebtPaymentEntity(customerId = customerId, shopId = shopId, amount = amount))
    }
}
