package com.kiranstore.manager.ui.screens.udhaar

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
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
            // Balance summary
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard(
                        title = "TOTAL OUTSTANDING", value = "₹${state.balance.toInt()}",
                        valueColor = if (state.balance > 0) OrangePrimary else Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "TOTAL PAYMENTS",
                        value = "₹${(state.payments.sumOf { it.amount }).toInt()}",
                        valueColor = Color(0xFF2E7D32), modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { /* open add debt */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) { Icon(Icons.Filled.Add, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Add Udhaar") }
                    OutlinedButton(
                        onClick = { /* open add payment */ },
                        modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)
                    ) { Icon(Icons.Filled.CheckCircle, null, Modifier.size(18.dp), tint = Color(0xFF2E7D32)); Spacer(Modifier.width(4.dp)); Text("Add Payment", color = Color(0xFF2E7D32)) }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Ledger header
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Detailed Ledger: ${state.customer?.name ?: ""}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            if (state.debts.isEmpty()) {
                item { EmptyState(Icons.Filled.Receipt, "No entries yet", "Add udhaar using the button above") }
            } else {
                items(state.debts, key = { it.id }) { debt ->
                    DebtLedgerCard(debt, onMarkPaid = { vm.markAsPaid(it) })
                }
            }
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
