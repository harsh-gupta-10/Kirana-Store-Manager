package com.kiranstore.manager.ui.screens.udhar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.data.database.entities.Customer
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CustomerViewModel
import com.kiranstore.manager.viewmodel.UdhaarViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUdhaarScreen(
    preselectedCustomerId: Long = -1L,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    udhaarViewModel: UdhaarViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel()
) {
    val allCustomers by customerViewModel.customers.collectAsState()

    var selectedCustomer by remember { mutableStateOf<Customer?>(null) }
    var customerDropdownExpanded by remember { mutableStateOf(false) }
    var customerSearch by remember { mutableStateOf("") }

    var itemName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var dateText by remember {
        mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
    }

    var showAddCustomerDialog by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    // Pre-select customer
    LaunchedEffect(preselectedCustomerId, allCustomers) {
        if (preselectedCustomerId > 0 && selectedCustomer == null) {
            selectedCustomer = allCustomers.firstOrNull { it.id == preselectedCustomerId }
        }
    }

    val filteredCustomers = if (customerSearch.isBlank()) allCustomers
    else allCustomers.filter {
        it.name.contains(customerSearch, ignoreCase = true) ||
                it.phone.contains(customerSearch)
    }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("Add Udhaar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Customer Dropdown
                    ExposedDropdownMenuBox(
                        expanded = customerDropdownExpanded,
                        onExpandedChange = { customerDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCustomer?.name ?: customerSearch,
                            onValueChange = {
                                customerSearch = it
                                selectedCustomer = null
                                customerDropdownExpanded = true
                            },
                            label = { Text("Customer *") },
                            leadingIcon = {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = OrangePrimary)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerDropdownExpanded) },
                            isError = errors.containsKey("customer"),
                            supportingText = { errors["customer"]?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        ExposedDropdownMenu(
                            expanded = customerDropdownExpanded,
                            onDismissRequest = { customerDropdownExpanded = false }
                        ) {
                            filteredCustomers.forEach { customer ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(customer.name, fontWeight = FontWeight.Medium)
                                            Text(customer.phone, fontSize = 12.sp, color = TextSecondary)
                                        }
                                    },
                                    onClick = {
                                        selectedCustomer = customer
                                        customerSearch = customer.name
                                        customerDropdownExpanded = false
                                        errors = errors - "customer"
                                    }
                                )
                            }
                            // Add new customer option
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Icon(Icons.Filled.PersonAdd, contentDescription = null,
                                            tint = OrangePrimary, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("+ Add New Customer", color = OrangePrimary,
                                            fontWeight = FontWeight.SemiBold)
                                    }
                                },
                                onClick = {
                                    customerDropdownExpanded = false
                                    showAddCustomerDialog = true
                                }
                            )
                        }
                    }

                    // Item Name
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it; errors = errors - "itemName" },
                        label = { Text("Item Name *") },
                        leadingIcon = {
                            Icon(Icons.Filled.ShoppingBag, contentDescription = null, tint = OrangePrimary)
                        },
                        placeholder = { Text("e.g. Rice 5kg, Sugar 2kg") },
                        isError = errors.containsKey("itemName"),
                        supportingText = { errors["itemName"]?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    // Amount
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it; errors = errors - "amount" },
                        label = { Text("Amount (₹) *") },
                        leadingIcon = {
                            Icon(Icons.Filled.CurrencyRupee, contentDescription = null, tint = OrangePrimary)
                        },
                        isError = errors.containsKey("amount"),
                        supportingText = { errors["amount"]?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    // Date
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = { dateText = it },
                        label = { Text("Date") },
                        leadingIcon = {
                            Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = OrangePrimary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    // Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
                        leadingIcon = {
                            Icon(Icons.Filled.Notes, contentDescription = null, tint = OrangePrimary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2,
                        maxLines = 3
                    )
                }
            }

            // Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Cancel", fontSize = 15.sp) }

                Button(
                    onClick = {
                        val newErrors = mutableMapOf<String, String>()
                        if (selectedCustomer == null) newErrors["customer"] = "Select a customer"
                        if (itemName.isBlank()) newErrors["itemName"] = "Item name is required"
                        val parsedAmount = amount.toDoubleOrNull()
                        if (parsedAmount == null || parsedAmount <= 0)
                            newErrors["amount"] = "Enter a valid amount"
                        errors = newErrors
                        if (newErrors.isEmpty()) {
                            udhaarViewModel.saveDebt(
                                customerId = selectedCustomer!!.id,
                                itemName = itemName,
                                amount = parsedAmount!!,
                                date = System.currentTimeMillis(),
                                notes = notes,
                                onDone = onSaved
                            )
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Udhaar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Quick Add Customer Dialog
    if (showAddCustomerDialog) {
        QuickAddCustomerDialog(
            onDismiss = { showAddCustomerDialog = false },
            onSave = { name, phone ->
                customerViewModel.saveCustomer(name, phone, "") { newId ->
                    val newCustomer = Customer(id = newId, name = name, phone = phone)
                    selectedCustomer = newCustomer
                    customerSearch = name
                    showAddCustomerDialog = false
                }
            }
        )
    }
}

@Composable
private fun QuickAddCustomerDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Customer", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank() && phone.isNotBlank()) onSave(name, phone) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) { Text("Save & Select") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
