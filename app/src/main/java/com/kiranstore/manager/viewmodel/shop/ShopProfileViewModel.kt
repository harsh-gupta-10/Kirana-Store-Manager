package com.kiranstore.manager.viewmodel.shop

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.shop.Shop
import com.kiranstore.manager.repository.shop.ShopRepository
import com.kiranstore.manager.storage.ImageCompressionUtil
import com.kiranstore.manager.storage.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ShopProfileState(
    val isLoading: Boolean = false,
    val shop: Shop? = null,
    val hasProfile: Boolean? = null,
    val isSaving: Boolean = false,
    val isUploading: Boolean = false,
    val logoUrl: String? = null,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ShopProfileViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    private val storageRepository: StorageRepository,
    private val imageCompressionUtil: ImageCompressionUtil
) : ViewModel() {

    private val _state = MutableStateFlow(ShopProfileState())
    val state: StateFlow<ShopProfileState> = _state.asStateFlow()

    init {
        loadShopProfile()
    }

    fun loadShopProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val shop = shopRepository.getShopProfile()
                _state.value = _state.value.copy(
                    isLoading = false,
                    shop = shop,
                    hasProfile = shop != null,
                    logoUrl = shop?.logoUrl
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    hasProfile = false,
                    error = e.message ?: "Failed to load shop profile"
                )
            }
        }
    }

    fun uploadLogo(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUploading = true, error = null)
            try {
                val compressedData = imageCompressionUtil.compressToWebP(context, uri)
                val fileName = "logos/${UUID.randomUUID()}.webp"
                val publicUrl = storageRepository.uploadImage(fileName, compressedData)
                _state.value = _state.value.copy(
                    isUploading = false,
                    logoUrl = publicUrl
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUploading = false,
                    error = e.message ?: "Failed to upload image"
                )
            }
        }
    }

    fun saveShopProfile(
        shopName: String,
        ownerName: String,
        phoneNumber: String,
        address: String
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null, saveSuccess = false)
            try {
                val shop = Shop(
                    shopName = shopName,
                    ownerName = ownerName,
                    phoneNumber = phoneNumber,
                    address = address,
                    logoUrl = _state.value.logoUrl
                )
                val existingShop = _state.value.shop
                if (existingShop != null) {
                    val updated = shopRepository.updateShopProfile(
                        shop.copy(id = existingShop.id)
                    )
                    _state.value = _state.value.copy(
                        isSaving = false,
                        shop = updated,
                        hasProfile = true,
                        saveSuccess = true
                    )
                } else {
                    val created = shopRepository.createShopProfile(shop)
                    _state.value = _state.value.copy(
                        isSaving = false,
                        shop = created,
                        hasProfile = true,
                        saveSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = e.message ?: "Failed to save shop profile"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
