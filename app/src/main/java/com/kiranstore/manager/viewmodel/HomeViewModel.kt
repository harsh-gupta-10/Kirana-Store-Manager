package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import com.kiranstore.manager.data.database.entities.Customer
import com.kiranstore.manager.data.database.entities.Debt
import com.kiranstore.manager.data.repository.CustomerRepository
import com.kiranstore.manager.data.repository.DebtRepository
import com.kiranstore.manager.data.repository.PaymentRepository
import com.kiranstore.manager.data.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val debtRepository: DebtRepository,
    private val paymentRepository: PaymentRepository,
    private val rentalRepository: RentalRepository
) : ViewModel() {

    /**
     * Total Udhar amount (remaining balance) across all customers.
     * Formula: Total Remaining = SUM(all debts) - SUM(all payments)
     */
    val totalUdhaarAmount: Flow<Double> = debtRepository.getTotalRemainingBalance()
    val totalCustomers: Flow<List<Customer>> = customerRepository.getAllCustomers()
    val activeRentalCount: Flow<Int> = rentalRepository.getActiveRentalCount()
    val recentDebts: Flow<List<Debt>> = debtRepository.getRecentDebts()
    val recoveredToday: Flow<Double> = paymentRepository.getPaymentsRecoveredToday()
}
