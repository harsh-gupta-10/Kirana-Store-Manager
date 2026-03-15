package com.kiranstore.manager.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.AuthViewModel
import com.kiranstore.manager.ui.viewmodel.HomeViewModel
import com.kiranstore.manager.ui.viewmodel.VoiceViewModel
import com.kiranstore.manager.utils.toRupees

@Composable
fun HomeScreen(
    navController: NavController,
    homeVm: HomeViewModel = hiltViewModel(),
    authVm: AuthViewModel = hiltViewModel(),
    voiceVm: VoiceViewModel = hiltViewModel()
) {
    val state by homeVm.state.collectAsState()
    val authState by authVm.state.collectAsState()
    val voiceState by voiceVm.state.collectAsState()

    LaunchedEffect(authState.userId) {
        if (authState.userId.isNotBlank()) homeVm.init(authState.userId)
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ── TOP HEADER ──────────────────────────────────
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(OrangeContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Store, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(24.dp))
                            }
                            Column {
                                Text(state.shopName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text("नमस्ते / Welcome", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Row {
                            IconButton(onClick = {}) { Icon(Icons.Rounded.Notifications, contentDescription = "Alerts") }
                            IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) { Icon(Icons.Rounded.Settings, contentDescription = "Settings") }
                        }
                    }
                }
            }

            // ── OVERVIEW SECTION ────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Overview Today", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE8F5E9)) {
                        Text("LIVE", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold))
                    }
                }
            }

            // ── SUMMARY CARDS GRID ──────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(
                            title = "Total Udhaar",
                            value = state.totalUdhaar.toRupees(),
                            subtitle = "कुल उधार",
                            badge = "+₹1.2k",
                            icon = Icons.Filled.AccountBalanceWallet,
                            valueColor = OrangePrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.UDHAAR) }
                        )
                        SummaryCard(
                            title = "Active Rentals",
                            value = state.activeRentals.toString(),
                            subtitle = "किराए मशीन",
                            badge = "Active",
                            badgeColor = Color(0xFF2196F3),
                            icon = Icons.Filled.Build,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.RENTAL) }
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(
                            title = "Tasks Today",
                            value = state.tasksToday.toString(),
                            subtitle = "आज के काम",
                            badge = "${state.tasksToday} New",
                            badgeColor = Color(0xFF9C27B0),
                            icon = Icons.Filled.CheckCircle,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.TASKS) }
                        )
                        SummaryCard(
                            title = "Buy List",
                            value = "${state.buyListCount} items",
                            subtitle = "खरीदने का सामान",
                            badge = if (state.buyListCount > 10) "Low Stock" else "",
                            badgeColor = Color(0xFFE65100),
                            icon = Icons.Filled.ShoppingCart,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.BUY_LIST) }
                        )
                    }
                }
            }

            // ── QUICK ACTIONS ───────────────────────────────
            item { SectionHeader("Quick Actions", "जल्दी काम करें") }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickActionButton(
                            label = "Add Udhaar", subLabel = "नया उधार जोड़ें",
                            icon = Icons.Filled.PersonAdd, isPrimary = true,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.UDHAAR) }
                        )
                        QuickActionButton(
                            label = "Rent Machine", subLabel = "मशीन किराए पर दें",
                            icon = Icons.Filled.Key,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.RENTAL) }
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickActionButton(
                            label = "Add Task", subLabel = "नया काम जोड़ें",
                            icon = Icons.Filled.TaskAlt,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.TASKS) }
                        )
                        QuickActionButton(
                            label = "Add Buy Item", subLabel = "खरीद सूची में जोड़ें",
                            icon = Icons.Filled.AddShoppingCart,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Routes.BUY_LIST) }
                        )
                    }
                }
            }

            // ── RECENT UDHAAR ──────────────────────────────
            item { SectionHeader("Recent Udhaar", "हाल का उधार") }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Placeholder recent items (would be driven by VM in full implementation)
                    RecentUdhaarRow("RK", "Ramesh Kumar", "2 hours ago", -450.0)
                    RecentUdhaarRow("SS", "Sunita Sharma", "5 hours ago", +1000.0)
                }
            }
        }

        // ── VOICE FAB ────────────────────────────────────
        VoiceFab(
            isListening = voiceState.isListening,
            onClick = { if (voiceState.isListening) voiceVm.stopListening() else voiceVm.startListening() },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
        )

        // ── VOICE DIALOG ─────────────────────────────────
        VoiceCommandDialog(
            isListening = voiceState.isListening,
            recognizedText = voiceState.recognizedText,
            onDismiss = { voiceVm.reset() }
        )

        // ── SNACKBAR for action result ────────────────────
        if (voiceState.actionResult.isNotBlank()) {
            LaunchedEffect(voiceState.actionResult) {
                kotlinx.coroutines.delay(2500)
                voiceVm.reset()
            }
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                containerColor = Color(0xFF2E7D32)
            ) {
                Text(voiceState.actionResult, color = Color.White)
            }
        }
    }
}

@Composable
private fun RecentUdhaarRow(initials: String, name: String, time: String, amount: Double) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                AvatarCircle(name = initials, size = 40.dp, fontSize = 13.sp)
                Column {
                    Text(name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(
                "${if (amount < 0) "-" else "+"}₹${Math.abs(amount).toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (amount < 0) OrangePrimary else Color(0xFF2E7D32)
                )
            )
        }
    }
}
