package com.kiranstore.manager.ui.screens.rentals

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
import com.kiranstore.manager.ui.components.formatDate
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CustomerViewModel
import com.kiranstore.manager.viewmodel.RentalViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRentalScreen(
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: RentalViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel()
) {
    val allCustomers by customerViewModel.customers.collectAsState()
    val allMachines by viewModel.allMachines.collectAsState()

    var selectedCustomerId by remember { mutableStateOf<Long?>(null) }
    var selectedMachineId by remember { mutableStateOf<Long?>(null) }
    var customerExpanded by remember { mutableStateOf(false) }
    var machineExpanded by remember { mutableStateOf(false) }
    var rentAmount by remember { mutableStateOf("") }
    var depositAmount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val startDate = remember { System.currentTimeMillis() }
    var expectedReturnDate by remember { mutableStateOf(startDate + 7 * 86_400_000L) }

    val selectedCustomer = allCustomers.firstOrNull { it.id == selectedCustomerId }
    val selectedMachine = allMachines.firstOrNull { it.id == selectedMachineId }

    // Auto-fill rent/deposit from machine
    LaunchedEffect(selectedMachineId) {
        selectedMachine?.let {
            if (rentAmount.isBlank()) rentAmount = it.rentPrice.toString()
            if (depositAmount.isBlank()) depositAmount = it.deposit.toString()
        }
    }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("New Rental", fontWeight = FontWeight.Bold) },
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
                    // Customer dropdown
                    ExposedDropdownMenuBox(
                        expanded = customerExpanded,
                        onExpandedChange = { customerExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCustomer?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Customer *") },
                            leadingIcon = {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = OrangePrimary)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerExpanded) },
                            isError = errors.containsKey("customer"),
                            supportingText = { errors["customer"]?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = customerExpanded,
                            onDismissRequest = { customerExpanded = false }
                        ) {
                            allCustomers.forEach { c ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(c.name, fontWeight = FontWeight.Medium)
                                            Text(c.phone, fontSize = 12.sp, color = TextSecondary)
                                        }
                                    },
                                    onClick = {
                                        selectedCustomerId = c.id
                                        customerExpanded = false
                                        errors = errors - "customer"
                                    }
                                )
                            }
                        }
                    }

                    // Machine dropdown
                    ExposedDropdownMenuBox(
                        expanded = machineExpanded,
                        onExpandedChange = { machineExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedMachine?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Machine *") },
                            leadingIcon = {
                                Icon(Icons.Filled.Construction, contentDescription = null, tint = OrangePrimary)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = machineExpanded) },
                            isError = errors.containsKey("machine"),
                            supportingText = { errors["machine"]?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = machineExpanded,
                            onDismissRequest = { machineExpanded = false }
                        ) {
                            if (allMachines.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No machines added yet", color = TextSecondary) },
                                    onClick = { machineExpanded = false }
                                )
                            }
                            allMachines.forEach { m ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(m.name, fontWeight = FontWeight.Medium)
                                            Text("Rate: ₹${formatAmount(m.rentPrice)} | Deposit: ₹${formatAmount(m.deposit)}",
                                                fontSize = 12.sp, color = TextSecondary)
                                        }
                                    },
                                    onClick = {
                                        selectedMachineId = m.id
                                        rentAmount = m.rentPrice.toString()
                                        depositAmount = m.deposit.toString()
                                        machineExpanded = false
                                        errors = errors - "machine"
                                    }
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = rentAmount,
                            onValueChange = { rentAmount = it },
                            label = { Text("Rent (₹) *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = depositAmount,
                            onValueChange = { depositAmount = it },
                            label = { Text("Deposit (₹)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = sdf.format(Date(startDate)),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Start Date") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = sdf.format(Date(expectedReturnDate)),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Expected Return") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
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
                        if (selectedCustomerId == null) newErrors["customer"] = "Select customer"
                        if (selectedMachineId == null) newErrors["machine"] = "Select machine"
                        errors = newErrors
                        if (newErrors.isEmpty()) {
                            viewModel.startRental(
                                customerId = selectedCustomerId!!,
                                machineId = selectedMachineId!!,
                                rentAmount = rentAmount.toDoubleOrNull() ?: 0.0,
                                depositAmount = depositAmount.toDoubleOrNull() ?: 0.0,
                                startDate = startDate,
                                expectedReturnDate = expectedReturnDate,
                                notes = notes,
                                onDone = onSaved
                            )
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Icon(Icons.Filled.Construction, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Start Rental", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnRentalScreen(
    rentalId: Long,
    onCompleted: () -> Unit,
    onCancel: () -> Unit,
    viewModel: RentalViewModel = hiltViewModel()
) {
    var additionalCharges by remember { mutableStateOf("") }
    var damageNotes by remember { mutableStateOf("") }
    val returnDate = remember { System.currentTimeMillis() }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("Return Rental", fontWeight = FontWeight.Bold) },
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
                    OutlinedTextField(
                        value = formatDate(returnDate),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Return Date") },
                        leadingIcon = {
                            Icon(Icons.Filled.CalendarToday, contentDescription = null,
                                tint = OrangePrimary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = additionalCharges,
                        onValueChange = { additionalCharges = it },
                        label = { Text("Additional Charges (₹)") },
                        leadingIcon = {
                            Icon(Icons.Filled.CurrencyRupee, contentDescription = null,
                                tint = OrangePrimary)
                        },
                        placeholder = { Text("0") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = damageNotes,
                        onValueChange = { damageNotes = it },
                        label = { Text("Damage Notes") },
                        leadingIcon = {
                            Icon(Icons.Filled.Warning, contentDescription = null,
                                tint = AmberWarning)
                        },
                        placeholder = { Text("Any damage or notes...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 3
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
                        viewModel.completeRental(
                            rentalId = rentalId,
                            returnDate = returnDate,
                            additionalCharges = additionalCharges.toDoubleOrNull() ?: 0.0,
                            damageNotes = damageNotes,
                            onDone = onCompleted
                        )
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Complete Rental", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
