package com.kiranstore.manager.ui.screens.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.ui.components.KiranTopBar
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.AuthViewModel
import com.kiranstore.manager.ui.viewmodel.ShopViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authVm: AuthViewModel = hiltViewModel(),
    shopVm: ShopViewModel = hiltViewModel()
) {
    val authState by authVm.state.collectAsState()
    val shop by shopVm.shop.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState.userId) {
        if (authState.userId.isNotBlank()) shopVm.loadShop(authState.userId)
        if (!authState.isLoggedIn && authState.userId.isBlank()) {
            navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
        KiranTopBar(title = "Settings", showBack = true, onBack = { navController.popBackStack() })

        // Shop profile card
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = OrangePrimary)
        ) {
            Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Store, null, Modifier.size(32.dp), tint = Color.White)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(shop?.shopName ?: "My Shop", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color.White))
                    Text(shop?.ownerName ?: "", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f)))
                    Text(shop?.phone ?: "", style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.7f)))
                }
                IconButton(onClick = { navController.navigate(Routes.SHOP_SETUP) }) {
                    Icon(Icons.Filled.Edit, null, tint = Color.White)
                }
            }
        }

        // Settings sections
        SettingsSection(title = "Shop") {
            SettingsRow(Icons.Filled.Store, "Edit Shop Profile", subtitle = "Name, address, phone") { navController.navigate(Routes.SHOP_SETUP) }
            SettingsRow(Icons.Filled.Notifications, "Notifications", subtitle = "Reminders and alerts") { }
        }

        SettingsSection(title = "Data") {
            SettingsRow(Icons.Filled.People, "Customer Directory", subtitle = "Manage all customers") { navController.navigate(Routes.CUSTOMERS) }
            SettingsRow(Icons.Filled.Download, "Export Data", subtitle = "Download your records") { }
            SettingsRow(Icons.Filled.DeleteForever, "Clear Purchased Items", subtitle = "Remove bought items from list") { }
        }

        SettingsSection(title = "About") {
            SettingsRow(Icons.Filled.Info, "App Version", subtitle = "v1.0.0 — Kiran Store Manager") { }
            SettingsRow(Icons.Filled.Help, "Help & Support", subtitle = "How to use the app") { }
        }

        // Logout
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { showLogoutDialog = true }.padding(18.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Logout, null, tint = Color(0xFFC62828), modifier = Modifier.size(24.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Logout", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFFC62828)))
                    Text("Sign out of your account", style = MaterialTheme.typography.bodySmall, color = Color(0xFFC62828).copy(alpha = 0.7f))
                }
                Icon(Icons.Rounded.ChevronRight, null, tint = Color(0xFFC62828))
            }
        }
        Spacer(Modifier.height(32.dp))
    }

    // Logout confirm dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Filled.Logout, null, tint = Color(0xFFC62828), modifier = Modifier.size(32.dp)) },
            title = { Text("Logout?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout from ${shop?.shopName ?: "the app"}?") },
            confirmButton = {
                Button(
                    onClick = { authVm.logout(); showLogoutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) { Text("Yes, Logout") }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }
}

// ─────────────────────────────────────────────────────────
// SETTINGS SECTION GROUP
// ─────────────────────────────────────────────────────────
@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            title.uppercase(),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(content = content)
        }
    }
}

// ─────────────────────────────────────────────────────────
// SETTINGS ROW
// ─────────────────────────────────────────────────────────
@Composable
fun SettingsRow(icon: ImageVector, title: String, subtitle: String = "", onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(OrangeContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, Modifier.size(20.dp), tint = OrangePrimary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
            if (subtitle.isNotBlank()) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Rounded.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
}

private fun Modifier.clip(shape: androidx.compose.ui.graphics.Shape) =
    this.then(androidx.compose.ui.draw.clip(shape))
