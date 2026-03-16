package com.kiranstore.manager.ui.screens.customers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CustomerViewModel
import com.kiranstore.manager.viewmodel.RentalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreen(
    customerId: Long,
    onBack: () -> Unit,
    onAddUdhaar: () -> Unit,
    onAddPayment: () -> Unit,
    onAddRental: () -> Unit,
    viewModel: CustomerViewModel = hiltViewModel(),
    rentalViewModel: RentalViewModel = hiltViewModel()
) {
    LaunchedEffect(customerId) { viewModel.loadCustomer(customerId) }

    val customer by viewModel.selectedCustomer.collectAsState()
    val debts by viewModel.getDebtsForCustomer(customerId).collectAsState(initial = emptyList())
    val payments by viewModel.getPaymentsForCustomer(customerId).collectAsState(initial = emptyList())
    val rentals by viewModel.getRentalsForCustomer(customerId).collectAsState(initial = emptyList())
    val totalDebt by viewModel.getDebtTotalForCustomer(customerId).collectAsState(initial = 0.0)
    val totalPaid by viewModel.getPaymentTotalForCustomer(customerId).collectAsState(initial = 0.0)
    val remaining = totalDebt - totalPaid

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text(customer?.name ?: "Customer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Call, contentDescription = "Call", tint = OrangePrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Summary Cards
            item {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(
                            title = "Total Udhaar",
                            value = "₹${formatAmount(totalDebt)}",
                            valueColor = RedDanger,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Total Paid",
                            value = "₹${formatAmount(totalPaid)}",
                            valueColor = GreenSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(
                            title = "Remaining",
                            value = "₹${formatAmount(remaining)}",
                            valueColor = if (remaining > 0) RedDanger else GreenSuccess,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Active Rentals",
                            value = "${rentals.count { it.status.name == "ACTIVE" }}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Action Buttons
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAddUdhaar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Udhaar", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onAddPayment,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess)
                    ) {
                        Icon(Icons.Filled.Payments, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Payment", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onAddRental,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Icon(Icons.Filled.Construction, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Rental", fontSize = 13.sp)
                    }
                }
            }

            // Udhaar History
            item { SectionHeader("Udhaar History") }

            if (debts.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center) {
                        Text("No udhaar entries", color = TextSecondary)
                    }
                }
            } else {
                items(debts.size) { idx ->
                    val debt = debts[idx]
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 3.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(debt.itemName, fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp)
                                Text(formatDate(debt.date), fontSize = 12.sp, color = TextSecondary)
                            }
                            Text("₹${formatAmount(debt.amount)}",
                                color = RedDanger, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }

            // Payments History
            item { SectionHeader("Payments") }

            if (payments.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center) {
                        Text("No payments yet", color = TextSecondary)
                    }
                }
            } else {
                items(payments.size) { idx ->
                    val payment = payments[idx]
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 3.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(payment.notes.ifBlank { "Payment received" },
                                    fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(formatDate(payment.date), fontSize = 12.sp, color = TextSecondary)
                            }
                            Text("+₹${formatAmount(payment.amount)}",
                                color = GreenSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
