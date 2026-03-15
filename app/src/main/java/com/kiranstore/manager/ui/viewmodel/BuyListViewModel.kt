package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.data.repository.BuyListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BuyListUiState(
    val shopId: Long = 0L,
    val items: List<BuyListItemEntity> = emptyList(),
    val isAddDialogOpen: Boolean = false
)

@HiltViewModel
class BuyListViewModel @Inject constructor(private val repo: BuyListRepository) : ViewModel() {

    private val _state = MutableStateFlow(BuyListUiState())
    val state: StateFlow<BuyListUiState> = _state.asStateFlow()

    fun init(shopId: Long) {
        _state.update { it.copy(shopId = shopId) }
        viewModelScope.launch {
            repo.getAllItems(shopId).collect { items -> _state.update { it.copy(items = items) } }
        }
    }

    fun addItem(name: String, quantity: String, priority: String, notes: String) {
        val shopId = _state.value.shopId
        viewModelScope.launch {
            repo.addItem(BuyListItemEntity(shopId = shopId, name = name, quantity = quantity, priority = priority, notes = notes))
        }
    }

    fun markAsPurchased(itemId: Long) = viewModelScope.launch { repo.markAsPurchased(itemId) }
    fun clearPurchased() = viewModelScope.launch { repo.clearPurchased(_state.value.shopId) }
    fun deleteItem(item: BuyListItemEntity) = viewModelScope.launch { repo.deleteItem(item) }
    fun openAddDialog() = _state.update { it.copy(isAddDialogOpen = true) }
    fun closeAddDialog() = _state.update { it.copy(isAddDialogOpen = false) }
}
