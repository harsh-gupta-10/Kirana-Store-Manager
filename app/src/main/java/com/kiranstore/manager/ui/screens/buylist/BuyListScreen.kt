package com.kiranstore.manager.ui.screens.buylist

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.BuyItem
import com.kiranstore.manager.viewmodel.BuyListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyListScreen(
    viewModel: BuyListViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val pendingItems = items.filter { !it.isBought }
    val boughtItems = items.filter { it.isBought }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Buy List", fontWeight = FontWeight.Bold)
                        Text("खरीदने का सामान", fontSize = 12.sp, color = TextSecondary)
                    }
                },
                actions = {
                    if (boughtItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearBoughtItems() }) {
                            Icon(Icons.Filled.CleaningServices, contentDescription = "Clear bought",
                                tint = TextSecondary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = OrangePrimary,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Item",
                    tint = CardWhite, modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Summary Cards
            item {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("TO BUY", fontSize = 10.sp, color = TextSecondary,
                                fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("${pendingItems.size}", fontSize = 24.sp,
                                fontWeight = FontWeight.Bold, color = AmberWarning)
                            Text("खरीदना बाकी", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("BOUGHT", fontSize = 10.sp, color = TextSecondary,
                                fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("${boughtItems.size}", fontSize = 24.sp,
                                fontWeight = FontWeight.Bold, color = GreenSuccess)
                            Text("खरीदा हुआ", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }

            // Pending Items Section
            if (pendingItems.isNotEmpty()) {
                item {
                    Text(
                        "TO BUY",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = TextSecondary, letterSpacing = 1.sp
                    )
                }
                items(pendingItems, key = { it.id }) { item ->
                    BuyItemRow(
                        item = item,
                        onToggle = { viewModel.toggleItem(item.id) },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                }
            }

            // Bought Items Section
            if (boughtItems.isNotEmpty()) {
                item {
                    Text(
                        "BOUGHT",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = TextSecondary, letterSpacing = 1.sp
                    )
                }
                items(boughtItems, key = { it.id }) { item ->
                    BuyItemRow(
                        item = item,
                        onToggle = { viewModel.toggleItem(item.id) },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                }
            }

            // Empty State
            if (items.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null,
                                tint = TextSecondary, modifier = Modifier.size(56.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("Buy list is empty", fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp, color = TextPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text("Tap + to add items to buy",
                                fontSize = 14.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }

    // Add Item Dialog
    if (showAddDialog) {
        AddBuyItemDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, quantity ->
                viewModel.addItem(name, quantity)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun BuyItemRow(
    item: BuyItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (item.isBought) GreenLight else CardWhite,
        label = "buyBg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isBought,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = GreenSuccess,
                    uncheckedColor = AmberWarning
                ),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = if (item.isBought) FontWeight.Normal else FontWeight.Medium,
                    color = if (item.isBought) TextSecondary else TextPrimary,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
                )
                if (item.quantity.isNotBlank()) {
                    Text(
                        text = item.quantity,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete",
                    tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Item?") },
            text = { Text("Remove \"${item.name}\" from the buy list?") },
            confirmButton = {
                Button(
                    onClick = { showDeleteConfirm = false; onDelete() },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDanger)
                ) { Text("Delete") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun AddBuyItemDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Add Item", fontWeight = FontWeight.Bold)
                Text("खरीद सूची में जोड़ें", fontSize = 12.sp, color = TextSecondary)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = false },
                    label = { Text("Item Name *") },
                    placeholder = { Text("e.g. Rice 5kg") },
                    isError = error,
                    supportingText = { if (error) Text("Item name is required") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (Optional)") },
                    placeholder = { Text("e.g. 2 bags") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) error = true
                    else onAdd(name, quantity)
                },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Item", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) { Text("Cancel") }
        }
    )
}
