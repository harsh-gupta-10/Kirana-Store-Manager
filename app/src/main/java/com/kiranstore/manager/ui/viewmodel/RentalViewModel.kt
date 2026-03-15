package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RentalWithCustomer(
    val rental: RentalEntity,
    val customer: CustomerEntity?
)

data class RentalUiState(
    val shopId: Long = 0L,
    val rentals: List<RentalWithCustomer> = emptyList(),
    val machines: List<RentalMachineEntity> = emptyList(),
    val activeRentalCount: Int = 0,
    val totalDeposits: Double = 0.0,
    val isAddRentalDialogOpen: Boolean = false,
    val isAddMachineDialogOpen: Boolean = false,
    val selectedTab: Int = 0,
    val error: String = ""
) {
    val activeRentals = rentals.filter { it.rental.status == RentalStatus.ACTIVE }
    val lateRentals = rentals.filter { it.rental.status == RentalStatus.LATE }
    val returnedRentals = rentals.filter { it.rental.status == RentalStatus.RETURNED }
}

@HiltViewModel
class RentalViewModel @Inject constructor(
    private val rentalRepo: RentalRepository,
    private val customerRepo: CustomerRepository,
    private val shopRepo: ShopRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RentalUiState())
    val state: StateFlow<RentalUiState> = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun init(shopId: Long) {
        _state.update { it.copy(shopId = shopId) }
        viewModelScope.launch {
            // Combine rentals with customer info
            rentalRepo.getAllRentals(shopId).flatMapLatest { rentals ->
                customerRepo.getAllCustomers(shopId).map { customers ->
                    val customerMap = customers.associateBy { it.id }
                    rentals.map { rental ->
                        RentalWithCustomer(rental, customerMap[rental.customerId])
                    }
                }
            }.collect { list ->
                _state.update { it.copy(rentals = list) }
            }
        }
        
        viewModelScope.launch {
            launch { rentalRepo.getAllMachines(shopId).collect { list -> _state.update { it.copy(machines = list) } } }
            launch { rentalRepo.getActiveRentalCount(shopId).collect { count -> _state.update { it.copy(activeRentalCount = count) } } }
            launch { rentalRepo.getTotalDepositsHeld(shopId).collect { amt -> _state.update { it.copy(totalDeposits = amt ?: 0.0) } } }
        }
    }

    fun addMachine(name: String, price: Double, deposit: Double) {
        viewModelScope.launch {
            rentalRepo.addMachine(RentalMachineEntity(shopId = _state.value.shopId, machineName = name, rentPrice = price, deposit = deposit))
            _state.update { it.copy(isAddMachineDialogOpen = false) }
        }
    }

    fun addRental(customerId: Long, machineName: String, deposit: Double, rentAmount: Double) {
        viewModelScope.launch {
            rentalRepo.addRental(RentalEntity(
                customerId = customerId, shopId = _state.value.shopId,
                machineName = machineName, deposit = deposit, rentAmount = rentAmount
            ))
            _state.update { it.copy(isAddRentalDialogOpen = false) }
        }
    }

    fun updateStatus(rentalId: Long, status: String) {
        viewModelScope.launch {
            rentalRepo.updateStatus(rentalId, status, if (status == RentalStatus.RETURNED) System.currentTimeMillis() else null)
        }
    }

    fun openAddDialog() = _state.update { it.copy(isAddRentalDialogOpen = true) }
    fun closeAddRental() = _state.update { it.copy(isAddRentalDialogOpen = false) }
    fun openAddMachine() = _state.update { it.copy(isAddMachineDialogOpen = true) }
    fun closeAddMachine() = _state.update { it.copy(isAddMachineDialogOpen = false) }
    
    fun selectTab(index: Int) {
        _state.update { it.copy(selectedTab = index) }
    }
}
