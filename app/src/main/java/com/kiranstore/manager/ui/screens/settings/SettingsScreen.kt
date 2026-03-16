package com.kiranstore.manager.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.data.remote.AuthState
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToLogin: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var dailyReminder by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Settings", fontWeight = FontWeight.Bold)
                        Text("सेटिंग्स", fontSize = 12.sp, color = TextSecondary)
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Store Info Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Store, contentDescription = null,
                            tint = OrangePrimary, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Kiran General Store",
                                fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("किरण जनरल स्टोर",
                                fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Divider(color = DividerColor)
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Phone, contentDescription = null,
                            tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Store Phone: Not set", fontSize = 14.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null,
                            tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Address: Not set", fontSize = 14.sp, color = TextSecondary)
                    }
                }
            }

            // Cloud Account Section
            SectionLabel("Cloud Account / क्लाउड खाता")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    when (val state = authState) {
                        is AuthState.Authenticated -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.CloudDone, contentDescription = null,
                                    tint = GreenSuccess, modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Connected", fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp, color = GreenSuccess)
                                    Text(state.userEmail, fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                            Divider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsClickRow(
                                icon = Icons.Filled.Sync,
                                title = "Sync Now",
                                subtitle = "अभी सिंक करें",
                                onClick = {}
                            )
                            Divider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsClickRow(
                                icon = Icons.Filled.Logout,
                                title = "Sign Out",
                                subtitle = "साइन आउट करें",
                                onClick = { authViewModel.signOut() }
                            )
                        }
                        else -> {
                            SettingsClickRow(
                                icon = Icons.Filled.CloudOff,
                                title = "Sign In to Cloud",
                                subtitle = "क्लाउड में साइन इन करें",
                                onClick = onNavigateToLogin
                            )
                        }
                    }
                }
            }

            // Notifications Section
            SectionLabel("Notifications / सूचनाएँ")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    SettingsToggleRow(
                        icon = Icons.Filled.Notifications,
                        title = "Notifications",
                        subtitle = "सूचनाएँ चालू करें",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                    Divider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsToggleRow(
                        icon = Icons.Filled.Alarm,
                        title = "Daily Reminder",
                        subtitle = "रोज़ याद दिलाएँ",
                        checked = dailyReminder,
                        onCheckedChange = { dailyReminder = it }
                    )
                }
            }

            // Appearance Section
            SectionLabel("Appearance / दिखावट")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                SettingsToggleRow(
                    icon = Icons.Filled.DarkMode,
                    title = "Dark Mode",
                    subtitle = "डार्क मोड (Coming Soon)",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }

            // Data Section
            SectionLabel("Data / डेटा")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    SettingsClickRow(
                        icon = Icons.Filled.Backup,
                        title = "Backup Data",
                        subtitle = "डेटा बैकअप लें",
                        onClick = {}
                    )
                    Divider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsClickRow(
                        icon = Icons.Filled.Restore,
                        title = "Restore Data",
                        subtitle = "डेटा वापस लाएँ",
                        onClick = {}
                    )
                }
            }

            // About Section
            SectionLabel("About / जानकारी")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    SettingsClickRow(
                        icon = Icons.Filled.Info,
                        title = "App Version",
                        subtitle = "1.0.0",
                        onClick = {}
                    )
                    Divider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsClickRow(
                        icon = Icons.Filled.Help,
                        title = "Help & Support",
                        subtitle = "मदद और सहायता",
                        onClick = {}
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionLabel(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = TextSecondary
    )
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = OrangePrimary,
            modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary)
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CardWhite,
                checkedTrackColor = OrangePrimary,
                uncheckedThumbColor = CardWhite,
                uncheckedTrackColor = DividerColor
            )
        )
    }
}

@Composable
private fun SettingsClickRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = OrangePrimary,
            modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary)
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null,
            tint = TextSecondary, modifier = Modifier.size(20.dp))
    }
}
