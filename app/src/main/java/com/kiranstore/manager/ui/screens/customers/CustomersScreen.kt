package com.kiranstore.manager.ui.screens.customers

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.data.database.entities.CustomerEntity
import com.kiranstore.manager.services.contacts.PhoneContact
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen(navController: NavController, vm: CustomerViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.init(1L) }

    // Contact permission launcher
    val contactPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) { vm.loadPhoneContacts(); vm.openContactPicker() }
    }

    Scaffold(
        topBar = {
            Column {
                KiranTopBar(
                    title = "Customer Directory",
                    showBack = true,
                    onBack = { navController.popBackStack() },
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Rounded.Notifications, null) }
                        IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) { Icon(Icons.Rounded.Settings, null) }
                    }
                )
                // Search bar
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { vm.search(it) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search contacts by name or number") },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangePrimary,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Action buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Import Contacts
                    OutlinedButton(
                        onClick = {
                            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, OrangePrimary),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = OrangeContainer)
                    ) {
                        Icon(Icons.Filled.Contacts, null, Modifier.size(18.dp), tint = OrangePrimary)
                        Spacer(Modifier.width(6.dp))
                        Text("Import Contacts", color = OrangePrimary, fontWeight = FontWeight.SemiBold)
                    }
                    // Add Manually
                    Button(
                        onClick = { vm.openAddDialog() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Icon(Icons.Filled.PersonAdd, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add Manually", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Recent transactions section
            if (state.customers.isNotEmpty()) {
                item {
                    Text(
                        "RECENT TRANSACTIONS",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                    )
                }
                items(state.customers.take(2), key = { "recent_${it.id}" }) { customer ->
                    CustomerDirectoryRow(customer, onUdhaarClick = { navController.navigate(Routes.udhaarDetail(it)) })
                }
            }

            // All contacts header
            item {
                Text(
                    "ALL CONTACTS",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                )
            }

            if (state.customers.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Filled.People,
                        title = "No customers yet",
                        subtitle = "Import from contacts or add manually",
                        actionLabel = "Add Customer",
                        onAction = { vm.openAddDialog() }
                    )
                }
            } else {
                items(state.customers, key = { it.id }) { customer ->
                    CustomerDirectoryRow(customer, onUdhaarClick = { navController.navigate(Routes.udhaarDetail(it)) })
                }
            }
        }
    }

    // Add Customer Dialog
    if (state.isAddDialogOpen) {
        AddCustomerDialog(
            onConfirm = { name, phone -> vm.addCustomer(name, phone); vm.closeAddDialog() },
            onDismiss = { vm.closeAddDialog() }
        )
    }

    // Contact Picker Bottom Sheet
    if (state.isContactPickerOpen) {
        ContactPickerSheet(
            contacts = state.phoneContacts,
            onSelect = { contact -> vm.importContact(contact); vm.closeContactPicker() },
            onDismiss = { vm.closeContactPicker() }
        )
    }
}

// ─────────────────────────────────────────────────────────
// CUSTOMER ROW
// ─────────────────────────────────────────────────────────
@Composable
fun CustomerDirectoryRow(customer: CustomerEntity, onUdhaarClick: (Long) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                AvatarCircle(customer.name, size = 46.dp)
                Column {
                    Text(customer.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(customer.phone.ifBlank { "No phone" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (customer.phone.isNotBlank()) {
                    IconButton(onClick = { /* dial */ }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Filled.Phone, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                // Udhaar chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = OrangeContainer,
                    modifier = Modifier.clickable { onUdhaarClick(customer.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.AccountBalanceWallet, null, Modifier.size(14.dp), tint = OrangePrimary)
                        Text("Udhaar", style = MaterialTheme.typography.labelMedium.copy(color = OrangePrimary, fontWeight = FontWeight.SemiBold))
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    }
}

// ─────────────────────────────────────────────────────────
// ADD CUSTOMER DIALOG
// ─────────────────────────────────────────────────────────
@Composable
fun AddCustomerDialog(onConfirm: (String, String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Customer", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KiranTextField(value = name, onValueChange = { name = it }, label = "Customer Name", leadingIcon = Icons.Filled.Person,
                    isError = name.isBlank(), errorText = "Name is required")
                KiranTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number", leadingIcon = Icons.Filled.Phone,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, phone) }, colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary), enabled = name.isNotBlank()) {
                Text("Add Customer")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

// ─────────────────────────────────────────────────────────
// CONTACT PICKER BOTTOM SHEET
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactPickerSheet(contacts: List<PhoneContact>, onSelect: (PhoneContact) -> Unit, onDismiss: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = if (searchQuery.isBlank()) contacts else contacts.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.phone.contains(searchQuery)
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text("Import from Contacts", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search contacts…") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                singleLine = true, shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OrangePrimary)
            )
            Spacer(Modifier.height(12.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                if (filtered.isEmpty()) {
                    item { Text("No contacts found", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
                } else {
                    items(filtered) { contact ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { onSelect(contact) }.padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AvatarCircle(contact.name, size = 40.dp, fontSize = 13.sp)
                            Column {
                                Text(contact.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text(contact.phone, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
