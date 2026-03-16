package com.kiranstore.manager.ui.screens.udhar

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
import com.kiranstore.manager.ui.components.formatAmount
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CustomerViewModel
import com.kiranstore.manager.viewmodel.UdhaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(
    preselectedCustomerId: Long = -1L,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    udhaarViewModel: UdhaarViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel()
) {
    val allCustomers by customerViewModel.customers.collectAsState()

    var selectedCustomerId by remember { mutableStateOf(preselectedCustomerId.takeIf { it > 0 }) }
    var customerDropdownExpanded by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val selectedCustomer = allCustomers.firstOrNull { it.id == selectedCustomerId }
    val totalDebt by remember(selectedCustomerId) {
        derivedStateOf {
            selectedCustomerId?.let { udhaarViewModel.getTotalDebtForCustomer(it) }
        }
    }
    val totalDebtVal by (totalDebt ?: kotlinx.coroutines.flow.flowOf(0.0))
        .collectAsState(initial = 0.0)
    val totalPaid by remember(selectedCustomerId) {
        derivedStateOf {
            selectedCustomerId?.let { udhaarViewModel.getTotalPaidForCustomer(it) }
        }
    }
    val totalPaidVal by (totalPaid ?: kotlinx.coroutines.flow.flowOf(0.0))
        .collectAsState(initial = 0.0)
    val remaining = totalDebtVal - totalPaidVal
    val parsedAmount = amount.toDoubleOrNull() ?: 0.0
    val afterPayment = remaining - parsedAmount

    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("Record Payment", fontWeight = FontWeight.Bold) },
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
            // Partial payment info card
            if (selectedCustomer != null && remaining > 0) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = OrangeLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Balance Summary", fontWeight = FontWeight.Bold,
                            color = OrangeDark, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()) {
                            Text("Total Debt:", color = TextSecondary, fontSize = 13.sp)
                            Text("₹${formatAmount(totalDebtVal)}", fontWeight = FontWeight.SemiBold,
                                color = RedDanger, fontSize = 13.sp)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()) {
                            Text("Already Paid:", color = TextSecondary, fontSize = 13.sp)
                            Text("₹${formatAmount(totalPaidVal)}", fontWeight = FontWeight.SemiBold,
                                color = GreenSuccess, fontSize = 13.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp),
                            color = OrangePrimary.copy(alpha = 0.2f))
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()) {
                            Text("Remaining:", color = TextPrimary, fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold)
                            Text("₹${formatAmount(remaining)}", fontWeight = FontWeight.Bold,
                                color = OrangePrimary, fontSize = 15.sp)
                        }
                        if (parsedAmount > 0) {
                            Divider(modifier = Modifier.padding(vertical = 4.dp),
                                color = OrangePrimary.copy(alpha = 0.2f))
                            Row(horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()) {
                                Text("After payment:", color = TextPrimary, fontSize = 13.sp)
                                Text("₹${formatAmount(afterPayment)}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (afterPayment <= 0) GreenSuccess else RedDanger,
                                    fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Customer dropdown
                    ExposedDropdownMenuBox(
                        expanded = customerDropdownExpanded,
                        onExpandedChange = { customerDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCustomer?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Customer *") },
                            leadingIcon = {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = OrangePrimary)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerDropdownExpanded) },
                            isError = errors.containsKey("customer"),
                            supportingText = { errors["customer"]?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = customerDropdownExpanded,
                            onDismissRequest = { customerDropdownExpanded = false }
                        ) {
                            allCustomers.forEach { customer ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(customer.name, fontWeight = FontWeight.Medium)
                                            Text(customer.phone, fontSize = 12.sp, color = TextSecondary)
                                        }
                                    },
                                    onClick = {
                                        selectedCustomerId = customer.id
                                        customerDropdownExpanded = false
                                        errors = errors - "customer"
                                    }
                                )
                            }
                        }
                    }

                    // Amount
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it; errors = errors - "amount" },
                        label = { Text("Payment Amount (₹) *") },
                        leadingIcon = {
                            Icon(Icons.Filled.CurrencyRupee, contentDescription = null, tint = GreenSuccess)
                        },
                        isError = errors.containsKey("amount"),
                        supportingText = { errors["amount"]?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                        minLines = 2
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Cancel", fontSize = 15.sp) }

                Button(
                    onClick = {
                        val newErrors = mutableMapOf<String, String>()
                        if (selectedCustomerId == null) newErrors["customer"] = "Select a customer"
                        val parsed = amount.toDoubleOrNull()
                        if (parsed == null || parsed <= 0) newErrors["amount"] = "Enter a valid amount"
                        errors = newErrors
                        if (newErrors.isEmpty()) {
                            udhaarViewModel.savePayment(
                                customerId = selectedCustomerId!!,
                                amount = parsed!!,
                                date = System.currentTimeMillis(),
                                notes = notes,
                                onDone = onSaved
                            )
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess)
                ) {
                    Icon(Icons.Filled.Payments, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Payment", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
