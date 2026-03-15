package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.CustomerEntity
import com.kiranstore.manager.data.repository.CustomerRepository
import com.kiranstore.manager.services.contacts.ContactsService
import com.kiranstore.manager.services.contacts.PhoneContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerUiState(
    val shopId: Long = 0L,
    val customers: List<CustomerEntity> = emptyList(),
    val phoneContacts: List<PhoneContact> = emptyList(),
    val searchQuery: String = "",
    val isAddDialogOpen: Boolean = false,
    val isContactPickerOpen: Boolean = false
)

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepo: CustomerRepository,
    private val contactsService: ContactsService
) : ViewModel() {

    private val _state = MutableStateFlow(CustomerUiState())
    val state: StateFlow<CustomerUiState> = _state.asStateFlow()

    fun init(shopId: Long) {
        _state.update { it.copy(shopId = shopId) }
        viewModelScope.launch {
            customerRepo.getAllCustomers(shopId).collect { list ->
                _state.update { it.copy(customers = list) }
            }
        }
    }

    fun search(query: String) {
        val shopId = _state.value.shopId
        _state.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            if (query.isBlank()) customerRepo.getAllCustomers(shopId).collect { list -> _state.update { it.copy(customers = list) } }
            else customerRepo.searchCustomers(shopId, query).collect { list -> _state.update { it.copy(customers = list) } }
        }
    }

    fun addCustomer(name: String, phone: String) {
        val shopId = _state.value.shopId
        viewModelScope.launch {
            customerRepo.addCustomer(CustomerEntity(shopId = shopId, name = name, phone = phone))
        }
    }

    fun loadPhoneContacts() = viewModelScope.launch {
        val contacts = contactsService.getAllContacts()
        _state.update { it.copy(phoneContacts = contacts) }
    }

    fun importContact(contact: PhoneContact) = addCustomer(contact.name, contact.phone)

    fun openAddDialog() = _state.update { it.copy(isAddDialogOpen = true) }
    fun closeAddDialog() = _state.update { it.copy(isAddDialogOpen = false) }
    fun openContactPicker() = _state.update { it.copy(isContactPickerOpen = true) }
    fun closeContactPicker() = _state.update { it.copy(isContactPickerOpen = false) }
}
