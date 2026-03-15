package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.repository.*
import com.kiranstore.manager.utils.todayEndMs
import com.kiranstore.manager.utils.todayStartMs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val shopName: String = "My Store",
    val ownerName: String = "",
    val totalUdhaar: Double = 0.0,
    val activeRentals: Int = 0,
    val tasksToday: Int = 0,
    val buyListCount: Int = 0,
    val recentUdhaarItems: List<RecentUdhaarItem> = emptyList()
)

data class RecentUdhaarItem(
    val customerId: Long,
    val customerName: String,
    val amount: Double,
    val timeAgo: String,
    val isDebt: Boolean
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopRepo: ShopRepository,
    private val debtRepo: DebtRepository,
    private val rentalRepo: RentalRepository,
    private val taskRepo: TaskRepository,
    private val buyListRepo: BuyListRepository,
    private val customerRepo: CustomerRepository
) : ViewModel() {

    private val _shopId = MutableStateFlow(1L)
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun init(userId: String) {
        viewModelScope.launch {
            shopRepo.getShopByUserId(userId).collect { shop ->
                if (shop != null) {
                    _shopId.value = shop.id
                    _state.update { it.copy(shopName = shop.shopName, ownerName = shop.ownerName) }
                    collectDashboardData(shop.id)
                }
            }
        }
    }

    private fun collectDashboardData(shopId: Long) {
        val start = todayStartMs(); val end = todayEndMs()
        viewModelScope.launch {
            combine(
                debtRepo.getTotalOutstanding(shopId),
                rentalRepo.getActiveRentalCount(shopId),
                taskRepo.getTodayPendingCount(shopId, start, end),
                buyListRepo.getPendingCount(shopId)
            ) { udhaar, rentals, tasks, buys ->
                _state.update { it.copy(
                    totalUdhaar = udhaar ?: 0.0,
                    activeRentals = rentals,
                    tasksToday = tasks,
                    buyListCount = buys
                )}
            }.collect()
        }
    }
}
