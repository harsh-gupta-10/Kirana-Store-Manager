package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.Customer
import com.kiranstore.manager.data.database.entities.Debt
import com.kiranstore.manager.data.database.entities.Payment
import com.kiranstore.manager.data.repository.CustomerRepository
import com.kiranstore.manager.data.repository.DebtRepository
import com.kiranstore.manager.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerBalance(
    val customer: Customer,
    val totalDebt: Double,
    val totalPaid: Double,
    val remaining: Double,
    val lastEntryTime: Long
)

@HiltViewModel
class UdhaarViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val debtRepository: DebtRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    val totalOutstanding: Flow<Double> = debtRepository.getTotalAllDebts()
    val recoveredToday: Flow<Double> = paymentRepository.getPaymentsRecoveredToday()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val allCustomers: StateFlow<List<Customer>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) customerRepository.getAllCustomers()
            else customerRepository.searchCustomers(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(q: String) { _searchQuery.value = q }

    fun getDebtsForCustomer(customerId: Long): Flow<List<Debt>> =
        debtRepository.getDebtsForCustomer(customerId)

    fun getTotalDebtForCustomer(customerId: Long): Flow<Double> =
        debtRepository.getTotalDebtForCustomer(customerId)

    fun getTotalPaidForCustomer(customerId: Long): Flow<Double> =
        paymentRepository.getTotalPaymentsForCustomer(customerId)

    fun saveDebt(
        customerId: Long,
        itemName: String,
        amount: Double,
        date: Long,
        notes: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            debtRepository.insertDebt(
                Debt(customerId = customerId, itemName = itemName.trim(),
                    amount = amount, date = date, notes = notes.trim())
            )
            onDone()
        }
    }

    fun savePayment(
        customerId: Long,
        amount: Double,
        date: Long,
        notes: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            paymentRepository.insertPayment(
                Payment(customerId = customerId, amount = amount,
                    date = date, notes = notes.trim())
            )
            onDone()
        }
    }

    fun markDebtAsPaid(debt: Debt, onDone: () -> Unit) {
        viewModelScope.launch {
            paymentRepository.insertPayment(
                Payment(customerId = debt.customerId, amount = debt.amount,
                    notes = "Paid: ${debt.itemName}")
            )
            onDone()
        }
    }
}
