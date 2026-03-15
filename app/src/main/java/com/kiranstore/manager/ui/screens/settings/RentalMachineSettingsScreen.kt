package com.kiranstore.manager.ui.screens.settings

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.data.database.entities.RentalMachineEntity
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.RentalViewModel

@Composable
fun RentalMachineSettingsScreen(navController: NavController, vm: RentalViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    
    // Using a fixed shopId for now
    LaunchedEffect(Unit) { vm.init(1L) }

    Scaffold(
        topBar = {
            KiranTopBar(
                title = "Rental Machine Settings",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.openAddMachine() }, containerColor = OrangePrimary) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.machines.isEmpty()) {
                item { EmptyState(Icons.Filled.Build, "No Machines Added", "Add machines that you provide on rent") }
            } else {
                items(state.machines) { machine ->
                    MachineSettingsCard(machine)
                }
            }
        }
    }

    if (state.isAddMachineDialogOpen) {
        AddMachineDialog(
            onConfirm = { name, price, deposit -> vm.addMachine(name, price, deposit) },
            onDismiss = { vm.closeAddMachine() }
        )
    }
}

@Composable
fun MachineSettingsCard(machine: RentalMachineEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(machine.machineName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("Rent: ₹${machine.rentPrice.toInt()} | Deposit: ₹${machine.deposit.toInt()}", 
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AddMachineDialog(onConfirm: (String, Double, Double) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Rental Machine", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KiranTextField(value = name, onValueChange = { name = it }, label = "Machine Name", leadingIcon = Icons.Filled.Build)
                KiranTextField(value = price, onValueChange = { price = it }, label = "Rent Price (₹)", leadingIcon = Icons.Filled.CurrencyRupee, keyboardType = KeyboardType.Number)
                KiranTextField(value = deposit, onValueChange = { deposit = it }, label = "Security Deposit (₹)", leadingIcon = Icons.Filled.AccountBalanceWallet, keyboardType = KeyboardType.Number)
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, price.toDoubleOrNull() ?: 0.0, deposit.toDoubleOrNull() ?: 0.0) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                enabled = name.isNotBlank() && price.isNotBlank()
            ) { Text("Add Machine") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
