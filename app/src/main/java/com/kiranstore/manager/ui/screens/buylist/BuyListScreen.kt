package com.kiranstore.manager.ui.screens.buylist

import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.data.database.entities.BuyListItemEntity
import com.kiranstore.manager.data.database.entities.BuyPriority
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.BuyListViewModel

@Composable
fun BuyListScreen(navController: NavController, vm: BuyListViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val pendingItems = state.items.filter { !it.isPurchased }
    val boughtItems = state.items.filter { it.isPurchased }

    LaunchedEffect(Unit) { vm.init(1L) }

    Scaffold(
        topBar = {
            KiranTopBar(
                title = "Buy List",
                subtitle = "खरीदने का सामान",
                showBack = true,
                onBack = { navController.popBackStack() },
                actions = {
                    if (boughtItems.isNotEmpty()) {
                        TextButton(onClick = { vm.clearPurchased() }) {
                            Text("Clear Done", color = OrangePrimary, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.openAddDialog() }, containerColor = OrangePrimary) {
                Icon(Icons.Filled.Add, contentDescription = "Add item", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Stats strip
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        title = "To Buy",
                        value = pendingItems.size.toString(),
                        badge = if (pendingItems.size > 10) "Low Stock" else "",
                        badgeColor = Color(0xFFE65100),
                        icon = Icons.Filled.ShoppingCart,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Purchased",
                        value = boughtItems.size.toString(),
                        badge = if (boughtItems.isNotEmpty()) "Done" else "",
                        badgeColor = Color(0xFF2E7D32),
                        icon = Icons.Filled.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Pending items
            if (pendingItems.isEmpty() && boughtItems.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Filled.ShoppingCart,
                        title = "Buy list is empty",
                        subtitle = "Tap + to add items you need to buy",
                        actionLabel = "Add Item",
                        onAction = { vm.openAddDialog() }
                    )
                }
            } else {
                if (pendingItems.isNotEmpty()) {
                    item {
                        Text(
                            "TO BUY (${pendingItems.size})",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                    items(pendingItems, key = { it.id }) { item ->
                        BuyListItemCard(item, onMarkPurchased = { vm.markAsPurchased(it) }, onDelete = { vm.deleteItem(it) })
                    }
                }

                if (boughtItems.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "PURCHASED (${boughtItems.size})",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Color(0xFF2E7D32))
                        )
                    }
                    items(boughtItems, key = { it.id }) { item ->
                        BuyListItemCard(item, onMarkPurchased = {}, onDelete = { vm.deleteItem(it) }, isDone = true)
                    }
                }
            }
        }
    }

    if (state.isAddDialogOpen) {
        AddBuyItemDialog(
            onConfirm = { name, qty, priority, notes ->
                vm.addItem(name, qty, priority, notes)
                vm.closeAddDialog()
            },
            onDismiss = { vm.closeAddDialog() }
        )
    }
}

// ─────────────────────────────────────────────────────────
// BUY LIST ITEM CARD
// ─────────────────────────────────────────────────────────
@Composable
fun BuyListItemCard(item: BuyListItemEntity, onMarkPurchased: (Long) -> Unit, onDelete: (BuyListItemEntity) -> Unit, isDone: Boolean = false) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) { onDelete(item); true } else false
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(Color(0xFFFFEBEE), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Filled.Delete, null, Modifier.padding(end = 20.dp), tint = Color(0xFFC62828))
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).alpha(if (isDone) 0.6f else 1f),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(if (isDone) 0.dp else 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { if (!isDone) onMarkPurchased(item.id) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF2E7D32),
                        uncheckedColor = if (item.priority == BuyPriority.HIGH) Color(0xFFC62828) else OrangePrimary
                    )
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Qty: ${item.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (item.notes.isNotBlank()) {
                            Text("• ${item.notes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                    }
                }
                StatusChip(item.priority)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// ADD BUY ITEM DIALOG
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBuyItemDialog(onConfirm: (String, String, String, String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(BuyPriority.MEDIUM) }
    var notes by remember { mutableStateOf("") }
    var priorityExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Buy List", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KiranTextField(value = name, onValueChange = { name = it }, label = "Item Name (e.g. Milk)", leadingIcon = Icons.Filled.ShoppingBag,
                    isError = name.isBlank(), errorText = "Item name required")
                KiranTextField(value = quantity, onValueChange = { quantity = it }, label = "Quantity (e.g. 20 packets)", leadingIcon = Icons.Filled.FormatListNumbered)
                // Priority dropdown
                ExposedDropdownMenuBox(expanded = priorityExpanded, onExpandedChange = { priorityExpanded = it }) {
                    OutlinedTextField(
                        value = priority, onValueChange = {}, readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(priorityExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OrangePrimary)
                    )
                    ExposedDropdownMenu(expanded = priorityExpanded, onDismissRequest = { priorityExpanded = false }) {
                        listOf(BuyPriority.HIGH, BuyPriority.MEDIUM, BuyPriority.LOW).forEach { p ->
                            DropdownMenuItem(text = { Text(p) }, onClick = { priority = p; priorityExpanded = false })
                        }
                    }
                }
                KiranTextField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)", singleLine = false)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, quantity, priority, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary), enabled = name.isNotBlank()) {
                Text("Add Item")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
