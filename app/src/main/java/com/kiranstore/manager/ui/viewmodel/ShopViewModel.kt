package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.ShopEntity
import com.kiranstore.manager.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopRepo: ShopRepository
) : ViewModel() {

    private val _shop = MutableStateFlow<ShopEntity?>(null)
    val shop: StateFlow<ShopEntity?> = _shop.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    fun loadShop(userId: String) {
        viewModelScope.launch {
            shopRepo.getShopByUserId(userId).collect { _shop.value = it }
        }
    }

    fun saveShop(shopName: String, ownerName: String, phone: String, address: String, userId: String) {
        viewModelScope.launch {
            val existing = _shop.value
            val entity = if (existing != null) existing.copy(shopName = shopName, ownerName = ownerName, phone = phone, address = address)
            else ShopEntity(shopName = shopName, ownerName = ownerName, phone = phone, address = address, userId = userId)
            shopRepo.saveShop(entity)
            _isSaved.value = true
        }
    }
}
