package com.kiranstore.manager.ui.screens.setup

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.AuthViewModel
import com.kiranstore.manager.ui.viewmodel.ShopViewModel

@Composable
fun ShopSetupScreen(
    navController: NavController,
    shopVm: ShopViewModel = hiltViewModel(),
    authVm: AuthViewModel = hiltViewModel()
) {
    val shopState by shopVm.isSaved.collectAsState()
    val authState by authVm.state.collectAsState()

    var shopName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(shopState) {
        if (shopState) navController.navigate(Routes.HOME) { popUpTo(Routes.SHOP_SETUP) { inclusive = true } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(modifier = Modifier.fillMaxWidth().background(OrangePrimary).padding(24.dp)) {
            Column {
                Icon(Icons.Filled.Store, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(36.dp))
                Spacer(Modifier.height(8.dp))
                Text("Setup Your Shop", style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White))
                Text("Tell us about your shop to get started", style = MaterialTheme.typography.bodyMedium.copy(
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f)))
            }
        }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Shop Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                    KiranTextField(value = shopName, onValueChange = { shopName = it },
                        label = "Shop Name (e.g. Kiran General Store)", leadingIcon = Icons.Filled.Store,
                        isError = shopName.isBlank(), errorText = "Shop name is required")

                    KiranTextField(value = ownerName, onValueChange = { ownerName = it },
                        label = "Owner Name", leadingIcon = Icons.Filled.Person)

                    KiranTextField(value = phone, onValueChange = { phone = it },
                        label = "Phone Number", leadingIcon = Icons.Filled.Phone,
                        keyboardType = KeyboardType.Phone)

                    KiranTextField(value = address, onValueChange = { address = it },
                        label = "Shop Address", leadingIcon = Icons.Filled.LocationOn,
                        singleLine = false)
                }
            }

            // Example hint
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = OrangeContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(20.dp))
                    Column {
                        Text("Example", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OrangeDark))
                        Text("Shop: Kiran General Store\nOwner: Vishnu\nPhone: 9876543210\nAddress: Vile Parle East, Mumbai",
                            style = MaterialTheme.typography.bodySmall, color = OrangeDark)
                    }
                }
            }

            PrimaryButton(
                label = "Start Managing Shop →",
                onClick = { shopVm.saveShop(shopName, ownerName, phone, address, authState.userId) },
                enabled = shopName.isNotBlank()
            )
        }
    }
}
