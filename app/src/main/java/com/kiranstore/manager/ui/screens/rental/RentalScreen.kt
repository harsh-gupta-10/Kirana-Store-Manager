package com.kiranstore.manager.ui.screens.rental

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

@Composable
fun RentalScreen(
    navController: NavController,
    vm: RentalViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val tabs = listOf("Active", "Overdue", "History")

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            KiranTopBar(
                title = "Rentals",
                showBack = false,
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Rounded.Search, null)
                    }
                }
            )
        }

        item {
            Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Button(
                    onClick = { /* New Rental */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("New Rental", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                }
                Spacer(Modifier.height(8.dp))
            }

            // ── Tabs ────────────────────────────────────────
            item {
                TabRow(
                    selectedTabIndex = state.selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = OrangePrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTab]),
                            color = OrangePrimary
                        )
                    }
                ) {
                    tabs.forEachIndexed { i, title ->
                        Tab(
                            selected = state.selectedTab == i,
                            onClick = { vm.selectTab(i) },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (state.selectedTab == i) FontWeight.Bold else FontWeight.Normal,
                                    color = if (state.selectedTab == i) OrangePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }
        }

        // ── List ─────────────────────────────────────────
        val filteredList = when (state.selectedTab) {
            0 -> state.rentals.filter { it.status == "ACTIVE" }
            1 -> state.rentals.filter { it.status == "OVERDUE" }
            else -> state.rentals.filter { it.status == "RETURNED" }
        }

        if (filteredList.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.Inventory2, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        Spacer(Modifier.height(12.dp))
                        Text("No rentals found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(filteredList) { rental ->
                RentalItem(rental) {
                    // Navigate to detail or mark as returned
                }
            }
        }
    }
}

@Composable
fun RentalItem(rental: Rental, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(OrangeContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Home, null, tint = OrangePrimary)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(rental.itemName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("To: ${rental.customerName}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(FormatUtils.formatCurrency(rental.dailyRate) + "/day", style = MaterialTheme.typography.titleMedium, color = OrangePrimary)
                Text(FormatUtils.formatDate(rental.startDate), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
