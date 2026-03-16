package com.kiranstore.manager.ui.screens.rentals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.data.database.entities.Rental
import com.kiranstore.manager.data.database.entities.RentalStatus
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.RentalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalsScreen(
    onAddRental: () -> Unit,
    onReturnRental: (Long) -> Unit,
    viewModel: RentalViewModel = hiltViewModel()
) {
    val activeRentals by viewModel.activeRentals.collectAsState()
    val returnedRentals by viewModel.returnedRentals.collectAsState()
    val lateRentals by viewModel.lateRentals.collectAsState()
    val totalDeposits by viewModel.totalDepositsHeld.collectAsState(initial = 0.0)
    val allMachines by viewModel.allMachines.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Active", "Returned", "Late")

    // Total machine capacity (max 20 for demo)
    val capacityPercent = if (allMachines.isNotEmpty())
        (activeRentals.size.toFloat() / allMachines.size.coerceAtLeast(1) * 100).coerceIn(0f, 100f)
    else 0f

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("Rental Management", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Settings, contentDescription = null, tint = TextSecondary)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Notifications, contentDescription = null, tint = TextSecondary)
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
            // Deposits Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(listOf(OrangePrimary, OrangeDark)))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Total Deposits Held", color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp)
                            Text("₹${formatAmount(totalDeposits)}",
                                color = Color.White, fontSize = 26.sp,
                                fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { capacityPercent / 100f },
                                modifier = Modifier.fillMaxWidth().height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("${capacityPercent.toInt()}% Capacity",
                                color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Icon(Icons.Filled.AccountBalanceWallet,
                            contentDescription = null, tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(36.dp))
                    }
                }
            }

            // New Rental Button
            item {
                Button(
                    onClick = onAddRental,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("New Rental", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
            }

            // Tabs
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = CardWhite,
                    contentColor = OrangePrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index)
                                        FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // Content per tab
            val currentRentals = when (selectedTab) {
                0 -> activeRentals
                1 -> returnedRentals
                2 -> lateRentals
                else -> activeRentals
            }

            if (currentRentals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Construction, contentDescription = null,
                                tint = TextSecondary, modifier = Modifier.size(52.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("No ${tabs[selectedTab].lowercase()} rentals",
                                color = TextSecondary, fontSize = 15.sp)
                        }
                    }
                }
            } else {
                items(currentRentals.size) { idx ->
                    val rental = currentRentals[idx]
                    RentalCard(
                        rental = rental,
                        viewModel = viewModel,
                        onViewDetails = {},
                        onReturn = { onReturnRental(rental.id) }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun RentalCard(
    rental: Rental,
    viewModel: RentalViewModel,
    onViewDetails: () -> Unit,
    onReturn: () -> Unit
) {
    val customer by viewModel.getCustomerById(rental.customerId).collectAsState(initial = null)
    val machine by viewModel.getMachineById(rental.machineId).collectAsState(initial = null)

    val isLate = rental.status == RentalStatus.LATE ||
            (rental.status == RentalStatus.ACTIVE &&
                    System.currentTimeMillis() > rental.expectedReturnDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Machine icon placeholder (colored header)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                if (isLate) RedDanger.copy(alpha = 0.15f) else OrangeLight,
                                BackgroundGrey
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Construction,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = if (isLate) RedDanger else OrangePrimary
                    )
                    Text(machine?.name ?: "Machine",
                        fontSize = 13.sp, color = TextSecondary,
                        fontWeight = FontWeight.Medium)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(machine?.name ?: "—",
                        fontWeight = FontWeight.Bold, fontSize = 18.sp,
                        modifier = Modifier.weight(1f))
                    if (isLate) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(RedLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            val daysLate = ((System.currentTimeMillis() - rental.expectedReturnDate) /
                                    86_400_000).toInt().coerceAtLeast(0)
                            Text("Late ($daysLate days)", fontSize = 11.sp,
                                color = RedDanger, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        StatusBadge(rental.status.name)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = TextSecondary, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(customer?.name ?: "—", fontSize = 14.sp, color = TextSecondary)
                }
                Spacer(Modifier.height(4.dp))
                if (isLate) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = null,
                            tint = RedDanger, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Due: ₹${formatAmount(rental.rentAmount)} Overdue",
                            fontSize = 13.sp, color = RedDanger, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CurrencyRupee, contentDescription = null,
                            tint = TextSecondary, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Daily Rate: ₹${formatAmount(rental.rentAmount)}",
                            fontSize = 14.sp, color = TextSecondary)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = if (rental.status == RentalStatus.ACTIVE) onReturn else onViewDetails,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLate) RedDanger else OrangePrimary
                        )
                    ) {
                        Text(
                            if (rental.status == RentalStatus.ACTIVE) {
                                if (isLate) "Contact Customer" else "View Details"
                            } else "View Details",
                            fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More",
                            tint = TextSecondary)
                    }
                }
            }
        }
    }
}
