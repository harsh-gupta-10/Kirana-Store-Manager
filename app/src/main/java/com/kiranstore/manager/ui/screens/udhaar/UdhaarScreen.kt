package com.kiranstore.manager.ui.screens.udhaar

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.*
import com.kiranstore.manager.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UdhaarScreen(navController: NavController, vm: UdhaarViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val customers = vm.filteredCustomers

    // Init with demo shopId=1; real app reads from DataStore
    LaunchedEffect(Unit) { vm.init(1L) }

    Scaffold(
        topBar = {
            Column {
                KiranTopBar(title = "Kiran General Store", subtitle = "Udhaar Manager",
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Rounded.Notifications, null) }
                        IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) { Icon(Icons.Rounded.Settings, null) }
                    }
                )
                // Search bar
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { vm.search(it) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search customers (e.g. Ramesh)") },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangePrimary,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.openAddDialog() }, containerColor = OrangePrimary) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
            // Summary row
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard(
                        title = "TOTAL OUTSTANDING", value = state.totalOutstanding.toRupees(),
                        valueColor = OrangePrimary, modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "RECOVERED TODAY", value = state.recoveredToday.toRupees(),
                        valueColor = Color(0xFF2E7D32), modifier = Modifier.weight(1f)
                    )
                }
            }

            // Active Balances header
            item { SectionHeader("Active Balances", "View History") }

            // Customer cards
            if (customers.isEmpty()) {
                item { EmptyState(Icons.Filled.AccountBalanceWallet, "No Udhaar yet", "Tap + to add your first udhaar entry") }
            } else {
                items(customers, key = { it.customer.id }) { cwb ->
                    CustomerBalanceCard(cwb, onClick = { navController.navigate(Routes.udhaarDetail(cwb.customer.id)) },
                        onAddPayment = { vm.openPaymentDialog(cwb.customer.id) })
                }
            }
        }
    }

    // Add Debt Dialog
    if (state.isAddDialogOpen) {
        AddDebtDialog(
            customers = state.customersWithBalance.map { it.customer },
            selectedCustomerId = state.selectedCustomerId,
            onConfirm = { customerId, itemName, amount, notes ->
                vm.addDebt(customerId, itemName, amount, notes)
                vm.closeAddDialog()
            },
            onDismiss = { vm.closeAddDialog() }
        )
    }

    // Add Payment Dialog
    if (state.isPaymentDialogOpen && state.selectedCustomerId != null) {
        AddPaymentDialog(
            customer = state.customersWithBalance.find { it.customer.id == state.selectedCustomerId }?.customer,
            onConfirm = { amount -> vm.addPayment(state.selectedCustomerId!!, amount); vm.closePaymentDialog() },
            onDismiss = { vm.closePaymentDialog() }
        )
    }
}

// ─────────────────────────────────────────────────────────
@Composable
fun CustomerBalanceCard(cwb: CustomerWithBalance, onClick: () -> Unit, onAddPayment: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                AvatarCircle(cwb.customer.name, size = 48.dp)
                Column {
                    Text(cwb.customer.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(if (cwb.balance == 0.0) "Settled" else "Last entry: recently",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Balance", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    cwb.balance.toRupees(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (cwb.balance == 0.0) Color(0xFF2E7D32) else OrangePrimary
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDebtDialog(customers: List<CustomerEntity>, selectedCustomerId: Long?, onConfirm: (Long, String, Double, String) -> Unit, onDismiss: () -> Unit) {
    var selectedId by remember { mutableStateOf(selectedCustomerId ?: customers.firstOrNull()?.id ?: 0L) }
    var itemName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val selectedCustomer = customers.find { it.id == selectedId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Udhaar", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Customer picker
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = selectedCustomer?.name ?: "Select Customer",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Customer") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OrangePrimary)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        customers.forEach { c ->
                            DropdownMenuItem(text = { Text(c.name) }, onClick = { selectedId = c.id; expanded = false })
                        }
                    }
                }
                KiranTextField(value = itemName, onValueChange = { itemName = it }, label = "Item Name", leadingIcon = Icons.Filled.ShoppingBag)
                KiranTextField(value = amount, onValueChange = { amount = it }, label = "Amount (₹)", leadingIcon = Icons.Filled.CurrencyRupee, keyboardType = KeyboardType.Number)
                KiranTextField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)", singleLine = false)
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedId, itemName, amount.toDoubleOrNull() ?: 0.0, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                enabled = selectedId > 0 && amount.isNotBlank()
            ) { Text("Add Udhaar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddPaymentDialog(customer: CustomerEntity?, onConfirm: (Double) -> Unit, onDismiss: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Payment", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Recording payment from: ${customer?.name ?: ""}", style = MaterialTheme.typography.bodyMedium)
                KiranTextField(value = amount, onValueChange = { amount = it }, label = "Amount Received (₹)",
                    leadingIcon = Icons.Filled.CurrencyRupee, keyboardType = KeyboardType.Number)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(amount.toDoubleOrNull() ?: 0.0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                enabled = amount.isNotBlank()
            ) { Text("Confirm Payment") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
