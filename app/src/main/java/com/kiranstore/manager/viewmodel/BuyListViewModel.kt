package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

data class BuyItem(
    val id: Long,
    val name: String,
    val quantity: String = "",
    val isBought: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@HiltViewModel
class BuyListViewModel @Inject constructor() : ViewModel() {

    private val idCounter = AtomicLong(0)

    private val _items = MutableStateFlow<List<BuyItem>>(emptyList())
    val items: StateFlow<List<BuyItem>> = _items.asStateFlow()

    fun addItem(name: String, quantity: String = "") {
        if (name.isBlank()) return
        _items.value = _items.value + BuyItem(
            id = idCounter.incrementAndGet(),
            name = name.trim(),
            quantity = quantity.trim()
        )
    }

    fun toggleItem(itemId: Long) {
        _items.value = _items.value.map {
            if (it.id == itemId) it.copy(isBought = !it.isBought) else it
        }
    }

    fun deleteItem(itemId: Long) {
        _items.value = _items.value.filter { it.id != itemId }
    }

    fun clearBoughtItems() {
        _items.value = _items.value.filter { !it.isBought }
    }
}
