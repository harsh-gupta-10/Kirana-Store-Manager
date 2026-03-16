package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.Customer
import com.kiranstore.manager.data.repository.CustomerRepository
import com.kiranstore.manager.data.repository.DebtRepository
import com.kiranstore.manager.data.repository.PaymentRepository
import com.kiranstore.manager.data.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val debtRepository: DebtRepository,
    private val paymentRepository: PaymentRepository,
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val customers: StateFlow<List<Customer>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) customerRepository.getAllCustomers()
            else customerRepository.searchCustomers(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCustomer = MutableStateFlow<Customer?>(null)
    val selectedCustomer: StateFlow<Customer?> = _selectedCustomer.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun getDebtTotalForCustomer(customerId: Long) =
        debtRepository.getTotalDebtForCustomer(customerId)

    fun getPaymentTotalForCustomer(customerId: Long) =
        paymentRepository.getTotalPaymentsForCustomer(customerId)

    /**
     * Gets the remaining balance (Udhar) for a specific customer.
     * Formula: Remaining = SUM(debts.amount) - SUM(payments.amount)
     */
    fun getRemainingBalanceForCustomer(customerId: Long) =
        debtRepository.getRemainingBalanceForCustomer(customerId)

    fun getDebtsForCustomer(customerId: Long) =
        debtRepository.getDebtsForCustomer(customerId)

    fun getPaymentsForCustomer(customerId: Long) =
        paymentRepository.getPaymentsForCustomer(customerId)

    fun getRentalsForCustomer(customerId: Long) =
        rentalRepository.getRentalsForCustomer(customerId)

    fun saveCustomer(name: String, phone: String, notes: String, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = customerRepository.insertCustomer(
                Customer(name = name.trim(), phone = phone.trim(), notes = notes.trim())
            )
            onResult(id)
        }
    }

    fun loadCustomer(customerId: Long) {
        viewModelScope.launch {
            _selectedCustomer.value = customerRepository.getCustomerById(customerId)
        }
    }
}
