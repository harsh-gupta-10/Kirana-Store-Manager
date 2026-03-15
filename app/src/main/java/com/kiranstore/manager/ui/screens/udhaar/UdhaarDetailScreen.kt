package com.kiranstore.manager.ui.screens.udhaar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.data.database.entities.DebtEntity
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.UdhaarDetailViewModel
import com.kiranstore.manager.utils.*

@Composable
fun UdhaarDetailScreen(navController: NavController, customerId: Long, vm: UdhaarDetailViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    LaunchedEffect(customerId) { vm.init(customerId) }

    Scaffold(
        topBar = {
            KiranTopBar(
                title = state.customer?.name ?: "Customer",
                subtitle = state.customer?.phone ?: "",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
                // Balance summary
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        val totalDebt = state.debts.sumOf { it.amount }
                        val totalPaid = state.payments.sumOf { it.amount }
                        
                        SummaryCard(
                            title = "TOTAL UDHAAR", 
                            value = "₹${totalDebt.toInt()}",
                            valueColor = OrangePrimary,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "TOTAL PAID",
                            value = "₹${totalPaid.toInt()}",
                            valueColor = Color(0xFF2E7D32), 
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = OrangeContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("REMAINING BALANCE", fontWeight = FontWeight.Bold, color = OrangeDark)
                            Text("₹${state.balance.toInt()}", fontWeight = FontWeight.Bold, color = OrangePrimary)
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { vm.openAddDebtDialog() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) { Icon(Icons.Filled.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Add Udhaar") }
                        OutlinedButton(
                            onClick = { vm.openAddPaymentDialog() },
                            modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)
                        ) { Icon(Icons.Filled.CheckCircle, null, Modifier.size(18.dp), tint = Color(0xFF2E7D32)); Spacer(Modifier.width(4.dp)); Text("Add Payment", color = Color(0xFF2E7D32)) }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                // Ledger header
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Detailed Ledger",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }

                if (state.debts.isEmpty() && state.payments.isEmpty()) {
                    item { EmptyState(Icons.Filled.Receipt, "No entries yet", "Add udhaar using the button above") }
                } else {
                    // Combine debts and payments for a unified ledger
                    val ledgerItems = (state.debts.map { it to true } + state.payments.map { it to false })
                        .sortedByDescending { if (it.second) (it.first as DebtEntity).date else (it.first as com.kiranstore.manager.data.database.entities.DebtPaymentEntity).date }

                    items(ledgerItems) { item ->
                        if (item.second) {
                            DebtLedgerCard(item.first as DebtEntity, onMarkPaid = { vm.markAsPaid(it) })
                        } else {
                            val payment = item.first as com.kiranstore.manager.data.database.entities.DebtPaymentEntity
                            PaymentLedgerCard(payment)
                        }
                    }
                }
            }
        }
    }

    if (state.isAddDebtDialogOpen) {
        AddDebtDetailDialog(
            onConfirm = { itemName, amount, notes -> vm.addDebt(itemName, amount, notes) },
            onDismiss = { vm.closeAddDebtDialog() }
        )
    }

    if (state.isAddPaymentDialogOpen) {
        AddPaymentDetailDialog(
            onConfirm = { amount -> vm.addPayment(amount) },
            onDismiss = { vm.closeAddPaymentDialog() }
        )
    }
}

@Composable
fun AddDebtDetailDialog(onConfirm: (String, Double, String) -> Unit, onDismiss: () -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Udhaar Entry", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KiranTextField(value = itemName, onValueChange = { itemName = it }, label = "Item Name", leadingIcon = Icons.Filled.ShoppingBag)
                KiranTextField(value = amount, onValueChange = { amount = it }, label = "Amount (₹)", leadingIcon = Icons.Filled.CurrencyRupee, keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                KiranTextField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)", singleLine = false)
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(itemName, amount.toDoubleOrNull() ?: 0.0, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                enabled = amount.isNotBlank()
            ) { Text("Save Entry") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddPaymentDetailDialog(onConfirm: (Double) -> Unit, onDismiss: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Payment", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KiranTextField(value = amount, onValueChange = { amount = it }, label = "Amount Received (₹)",
                    leadingIcon = Icons.Filled.CurrencyRupee, keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(amount.toDoubleOrNull() ?: 0.0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                enabled = amount.isNotBlank()
            ) { Text("Save Payment") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun PaymentLedgerCard(payment: com.kiranstore.manager.data.database.entities.DebtPaymentEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(payment.date.toDisplayDateTime(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Payment Received", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF2E7D32)))
            }
            Text("₹${payment.amount.toInt()}", style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)))
        }
    }
}

@Composable
fun DebtLedgerCard(debt: DebtEntity, onMarkPaid: (Long) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(debt.date.toDisplayDateTime(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("₹${debt.amount.toInt()}", style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold, color = OrangePrimary))
            }
            Spacer(Modifier.height(4.dp))
            Text(debt.itemName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            if (debt.notes.isNotBlank()) Text(debt.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            if (debt.status == "PENDING") {
                Button(
                    onClick = { onMarkPaid(debt.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Filled.CheckCircle, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Mark as Paid")
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.CheckCircle, null, Modifier.size(16.dp), tint = Color(0xFF2E7D32))
                    Text("Paid", color = Color(0xFF2E7D32), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold))
                }
            }
        }
    }
}
