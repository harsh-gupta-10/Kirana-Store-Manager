package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RentalWithCustomer(val rental: RentalEntity, val customer: CustomerEntity?)

data class RentalUiState(
    val shopId: Long = 0L,
    val activeRentals: List<RentalWithCustomer> = emptyList(),
    val returnedRentals: List<RentalWithCustomer> = emptyList(),
    val lateRentals: List<RentalWithCustomer> = emptyList(),
    val totalDeposits: Double = 0.0,
    val selectedTab: Int = 0,
    val isAddDialogOpen: Boolean = false
)

@HiltViewModel
class RentalViewModel @Inject constructor(
    private val rentalRepo: RentalRepository,
    private val customerRepo: CustomerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RentalUiState())
    val state: StateFlow<RentalUiState> = _state.asStateFlow()

    fun init(shopId: Long) {
        _state.update { it.copy(shopId = shopId) }
        viewModelScope.launch {
            launch {
                rentalRepo.getRentalsByStatus(shopId, RentalStatus.ACTIVE).collect { list ->
                    val enriched = list.map { r -> RentalWithCustomer(r, customerRepo.getById(r.customerId)) }
                    _state.update { it.copy(activeRentals = enriched) }
                }
            }
            launch {
                rentalRepo.getRentalsByStatus(shopId, RentalStatus.RETURNED).collect { list ->
                    val enriched = list.map { r -> RentalWithCustomer(r, customerRepo.getById(r.customerId)) }
                    _state.update { it.copy(returnedRentals = enriched) }
                }
            }
            launch {
                rentalRepo.getRentalsByStatus(shopId, RentalStatus.LATE).collect { list ->
                    val enriched = list.map { r -> RentalWithCustomer(r, customerRepo.getById(r.customerId)) }
                    _state.update { it.copy(lateRentals = enriched) }
                }
            }
            launch {
                rentalRepo.getTotalDepositsHeld(shopId).collect { total ->
                    _state.update { it.copy(totalDeposits = total ?: 0.0) }
                }
            }
        }
    }

    fun addRental(customerId: Long, machineName: String, deposit: Double, rentAmount: Double, returnDate: Long?) {
        val shopId = _state.value.shopId
        viewModelScope.launch {
            rentalRepo.addRental(RentalEntity(
                customerId = customerId, shopId = shopId,
                machineName = machineName, deposit = deposit,
                rentAmount = rentAmount, returnDate = returnDate
            ))
        }
    }

    fun markReturned(rentalId: Long) = viewModelScope.launch {
        rentalRepo.updateStatus(rentalId, RentalStatus.RETURNED, System.currentTimeMillis())
    }

    fun markLate(rentalId: Long) = viewModelScope.launch {
        rentalRepo.updateStatus(rentalId, RentalStatus.LATE, null)
    }

    fun selectTab(tab: Int) = _state.update { it.copy(selectedTab = tab) }
    fun openAddDialog() = _state.update { it.copy(isAddDialogOpen = true) }
    fun closeAddDialog() = _state.update { it.copy(isAddDialogOpen = false) }
}
