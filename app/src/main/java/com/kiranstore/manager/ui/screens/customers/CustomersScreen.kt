package com.kiranstore.manager.ui.screens.customers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.data.database.entities.Customer
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen(
    onCustomerClick: (Long) -> Unit,
    onAddCustomer: () -> Unit,
    onAddUdhaarForCustomer: (Long) -> Unit,
    viewModel: CustomerViewModel = hiltViewModel()
) {
    val customers by viewModel.customers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Group: recent (top 2) and all
    val recentCustomers = customers.take(2)
    val allCustomers = customers

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            Surface(shadowElevation = 2.dp, color = CardWhite) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        "Customer Directory",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Notifications, contentDescription = null,
                            tint = TextSecondary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Settings, contentDescription = null,
                            tint = TextSecondary)
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCustomer,
                containerColor = OrangePrimary
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = "Add Customer",
                    tint = CardWhite)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Search
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    placeholder = { Text("Search contacts by name or number") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null,
                            tint = TextSecondary)
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

            // Import / Add Manually buttons
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = OrangePrimary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Icon(Icons.Filled.ContactPage, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Import Contacts", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onAddCustomer,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Icon(Icons.Filled.PersonAdd, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add Manually", fontSize = 13.sp)
                    }
                }
            }

            // Recent Transactions header
            if (recentCustomers.isNotEmpty() && searchQuery.isBlank()) {
                item {
                    Text(
                        "RECENT TRANSACTIONS",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                }
                items(recentCustomers.size) { idx ->
                    CustomerRow(
                        customer = recentCustomers[idx],
                        onClick = { onCustomerClick(recentCustomers[idx].id) },
                        onUdhaarClick = { onAddUdhaarForCustomer(recentCustomers[idx].id) }
                    )
                    if (idx < recentCustomers.lastIndex) Divider(color = DividerColor, thickness = 0.5.dp)
                }
            }

            // All Contacts header
            item {
                Text(
                    "ALL CONTACTS",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }

            if (allCustomers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.People, contentDescription = null,
                                tint = TextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("No customers yet", color = TextSecondary)
                            Spacer(Modifier.height(4.dp))
                            Text("Tap + to add your first customer",
                                fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                }
            } else {
                items(allCustomers.size) { idx ->
                    CustomerRow(
                        customer = allCustomers[idx],
                        onClick = { onCustomerClick(allCustomers[idx].id) },
                        onUdhaarClick = { onAddUdhaarForCustomer(allCustomers[idx].id) }
                    )
                    if (idx < allCustomers.lastIndex) Divider(color = DividerColor, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun CustomerRow(
    customer: Customer,
    onClick: () -> Unit,
    onUdhaarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(CardWhite)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarCircle(name = customer.name, size = 44)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(customer.name, fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp, color = TextPrimary)
            Text(customer.phone, fontSize = 13.sp, color = TextSecondary)
        }
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Call, contentDescription = "Call",
                tint = TextPrimary, modifier = Modifier.size(20.dp))
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(OrangeLight)
                .clickable { onUdhaarClick() }
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Add, contentDescription = null,
                    tint = OrangePrimary, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("Udhaar", color = OrangePrimary,
                    fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
