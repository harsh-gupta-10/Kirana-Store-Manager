package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.Rental
import com.kiranstore.manager.data.database.entities.RentalMachine
import com.kiranstore.manager.data.database.entities.RentalStatus
import com.kiranstore.manager.data.repository.CustomerRepository
import com.kiranstore.manager.data.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentalViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {

    val activeRentals: StateFlow<List<Rental>> = rentalRepository
        .getRentalsByStatus(RentalStatus.ACTIVE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val returnedRentals: StateFlow<List<Rental>> = rentalRepository
        .getRentalsByStatus(RentalStatus.RETURNED)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lateRentals: StateFlow<List<Rental>> = rentalRepository
        .getRentalsByStatus(RentalStatus.LATE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMachines: StateFlow<List<RentalMachine>> = rentalRepository.getAllMachines()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalDepositsHeld: Flow<Double> = rentalRepository.getTotalDepositsHeld()
    val allCustomers = customerRepository.getAllCustomers()

    fun getCustomerById(id: Long) = customerRepository.getAllCustomers().map { list ->
        list.firstOrNull { it.id == id }
    }

    fun getMachineById(id: Long) = rentalRepository.getAllMachines().map { list ->
        list.firstOrNull { it.id == id }
    }

    fun startRental(
        customerId: Long,
        machineId: Long,
        rentAmount: Double,
        depositAmount: Double,
        startDate: Long,
        expectedReturnDate: Long,
        notes: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            rentalRepository.insertRental(
                Rental(
                    customerId = customerId,
                    machineId = machineId,
                    rentAmount = rentAmount,
                    depositAmount = depositAmount,
                    startDate = startDate,
                    expectedReturnDate = expectedReturnDate,
                    notes = notes
                )
            )
            onDone()
        }
    }

    fun completeRental(
        rentalId: Long,
        returnDate: Long,
        additionalCharges: Double,
        damageNotes: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val rental = rentalRepository.getRentalById(rentalId) ?: return@launch
            rentalRepository.updateRental(
                rental.copy(
                    actualReturnDate = returnDate,
                    additionalCharges = additionalCharges,
                    damageNotes = damageNotes,
                    status = RentalStatus.RETURNED
                )
            )
            onDone()
        }
    }

    fun addMachine(name: String, rentPrice: Double, deposit: Double, onDone: () -> Unit) {
        viewModelScope.launch {
            rentalRepository.insertMachine(
                RentalMachine(name = name.trim(), rentPrice = rentPrice, deposit = deposit)
            )
            onDone()
        }
    }
}
