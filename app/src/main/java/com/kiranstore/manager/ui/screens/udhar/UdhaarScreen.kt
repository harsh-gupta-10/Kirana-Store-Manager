package com.kiranstore.manager.ui.screens.udhar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.data.database.entities.Customer
import com.kiranstore.manager.data.database.entities.Debt
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.UdhaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UdhaarScreen(
    onAddUdhaar: () -> Unit,
    viewModel: UdhaarViewModel = hiltViewModel()
) {
    val totalOutstanding by viewModel.totalOutstanding.collectAsState(initial = 0.0)
    val recoveredToday by viewModel.recoveredToday.collectAsState(initial = 0.0)
    val customers by viewModel.allCustomers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Selected customer for ledger view
    var selectedCustomer by remember { mutableStateOf<Customer?>(null) }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            Surface(shadowElevation = 2.dp, color = CardWhite) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Store icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(OrangePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Store, contentDescription = null,
                            tint = CardWhite, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Kiran General Store", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = TextPrimary)
                        Text("Udhaar Manager", fontSize = 12.sp, color = TextSecondary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Notifications, contentDescription = null, tint = TextSecondary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Settings, contentDescription = null, tint = TextSecondary)
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddUdhaar,
                containerColor = OrangePrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Udhaar", tint = CardWhite)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    placeholder = { Text("Search customers (e.g. Ramesh)") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = CardWhite,
                        focusedContainerColor = CardWhite,
                        unfocusedBorderColor = DividerColor
                    ),
                    singleLine = true
                )
            }

            // Total Outstanding / Recovered Today cards
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("TOTAL OUTSTANDING", fontSize = 10.sp,
                                color = TextSecondary, fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("₹${formatAmount(totalOutstanding)}",
                                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = RedDanger)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("RECOVERED TODAY", fontSize = 10.sp,
                                color = TextSecondary, fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("₹${formatAmount(recoveredToday)}",
                                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                        }
                    }
                }
            }

            // Active Balances header
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Active Balances", fontWeight = FontWeight.Bold,
                        fontSize = 17.sp, modifier = Modifier.weight(1f))
                    Text("View History", color = OrangePrimary,
                        fontSize = 13.sp, fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { })
                }
            }

            // Customer balance rows
            if (customers.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null,
                                tint = TextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("No customers yet", color = TextSecondary)
                        }
                    }
                }
            } else {
                items(customers.size) { idx ->
                    val customer = customers[idx]
                    CustomerBalanceRow(
                        customer = customer,
                        viewModel = viewModel,
                        isSelected = selectedCustomer?.id == customer.id,
                        onClick = {
                            selectedCustomer = if (selectedCustomer?.id == customer.id) null
                            else customer
                        }
                    )
                }
            }

            // Detailed Ledger for selected customer
            selectedCustomer?.let { cust ->
                item {
                    Text(
                        "Detailed Ledger: ${cust.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    )
                }
                item {
                    DetailedLedgerSection(
                        customer = cust,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomerBalanceRow(
    customer: Customer,
    viewModel: UdhaarViewModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val totalDebt by viewModel.getTotalDebtForCustomer(customer.id).collectAsState(initial = 0.0)
    val totalPaid by viewModel.getTotalPaidForCustomer(customer.id).collectAsState(initial = 0.0)
    val remaining = totalDebt - totalPaid

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) OrangeLight else CardWhite
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            remaining > 0 -> OrangeLight
                            else -> GreenLight
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialsOf(customer.name).take(1),
                    color = if (remaining > 0) OrangePrimary else GreenSuccess,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(customer.name, fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp, color = TextPrimary)
                Text(
                    if (remaining == 0.0) "Settled yesterday" else "Last entry: recently",
                    fontSize = 12.sp, color = TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Balance", fontSize = 11.sp, color = TextSecondary)
                Text(
                    "₹${formatAmount(remaining)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (remaining > 0) RedDanger else GreenSuccess
                )
            }
        }
    }
}

@Composable
private fun DetailedLedgerSection(
    customer: Customer,
    viewModel: UdhaarViewModel
) {
    val debts by viewModel.getDebtsForCustomer(customer.id).collectAsState(initial = emptyList())

    if (debts.isEmpty()) {
        Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
            Text("No udhaar entries for ${customer.name}", color = TextSecondary)
        }
    } else {
        Column {
            debts.forEach { debt ->
                LedgerEntryCard(debt = debt, onMarkPaid = {
                    viewModel.markDebtAsPaid(debt) {}
                })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun LedgerEntryCard(
    debt: Debt,
    onMarkPaid: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formatDateTime(debt.date), fontSize = 12.sp, color = TextSecondary,
                    modifier = Modifier.weight(1f))
                Text("₹${formatAmount(debt.amount)}",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RedDanger)
            }
            Spacer(Modifier.height(6.dp))
            Text(debt.itemName, fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp, color = TextPrimary)
            if (debt.notes.isNotBlank()) {
                Text(debt.notes, fontSize = 12.sp, color = TextSecondary)
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess)
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Mark as Paid", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Mark as Paid?") },
            text = { Text("This will record a payment of ₹${formatAmount(debt.amount)} for ${debt.itemName}.") },
            confirmButton = {
                Button(
                    onClick = { showConfirmDialog = false; onMarkPaid() },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess)
                ) { Text("Mark Paid") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) { Text("Cancel") }
            }
        )
    }
}
