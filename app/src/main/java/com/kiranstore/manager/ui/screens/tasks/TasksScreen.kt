package com.kiranstore.manager.ui.screens.tasks

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.data.database.entities.TaskEntity
import com.kiranstore.manager.data.database.entities.TaskStatus
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.TaskViewModel
import com.kiranstore.manager.utils.toDisplayDate

@Composable
fun TasksScreen(navController: NavController, vm: TaskViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val pendingTasks = state.todayTasks.filter { it.status == TaskStatus.PENDING }
    val doneTasks = state.todayTasks.filter { it.status == TaskStatus.DONE }

    LaunchedEffect(Unit) { vm.init(1L) }

    Scaffold(
        topBar = {
            KiranTopBar(
                title = "Tasks Today",
                subtitle = "आज के काम",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.openAddDialog() }, containerColor = OrangePrimary) {
                Icon(Icons.Filled.Add, contentDescription = "Add task", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Progress card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OrangePrimary)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Today's Progress", style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                Text(System.currentTimeMillis().toDisplayDate(), style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f)))
                            }
                            Text(
                                "${doneTasks.size}/${state.todayTasks.size}",
                                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        val progress = if (state.todayTasks.isEmpty()) 0f else doneTasks.size.toFloat() / state.todayTasks.size
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("${(progress * 100).toInt()}% done", style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f)))
                    }
                }
            }

            if (state.todayTasks.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Filled.CheckCircle,
                        title = "No tasks for today",
                        subtitle = "Tap + to add tasks for the day",
                        actionLabel = "Add Task",
                        onAction = { vm.openAddDialog() }
                    )
                }
            } else {
                // Pending tasks
                if (pendingTasks.isNotEmpty()) {
                    item {
                        Text(
                            "PENDING (${pendingTasks.size})",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                    items(pendingTasks, key = { it.id }) { task ->
                        TaskCard(task, onMarkDone = { vm.markAsDone(it) }, onDelete = { vm.deleteTask(it) })
                    }
                }

                // Done tasks
                if (doneTasks.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "DONE (${doneTasks.size})",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Color(0xFF2E7D32))
                        )
                    }
                    items(doneTasks, key = { it.id }) { task ->
                        TaskCard(task, onMarkDone = {}, onDelete = { vm.deleteTask(it) }, isDone = true)
                    }
                }
            }
        }
    }

    if (state.isAddDialogOpen) {
        AddTaskDialog(
            onConfirm = { name, notes -> vm.addTask(name, notes); vm.closeAddDialog() },
            onDismiss = { vm.closeAddDialog() }
        )
    }
}

// ─────────────────────────────────────────────────────────
// TASK CARD
// ─────────────────────────────────────────────────────────
@Composable
fun TaskCard(task: TaskEntity, onMarkDone: (Long) -> Unit, onDelete: (TaskEntity) -> Unit, isDone: Boolean = false) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) { onDelete(task); true } else false
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
            elevation = CardDefaults.cardElevation(if (isDone) 0.dp else 2.dp),
            border = if (!isDone) null else BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated checkbox
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { if (!isDone) onMarkDone(task.id) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF2E7D32),
                        uncheckedColor = OrangePrimary
                    )
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        task.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (isDone) FontWeight.Normal else FontWeight.SemiBold,
                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                            color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                    )
                    if (task.notes.isNotBlank()) {
                        Text(task.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                    }
                }
                if (isDone) {
                    Icon(Icons.Filled.CheckCircle, null, Modifier.size(20.dp), tint = Color(0xFF2E7D32))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// ADD TASK DIALOG
// ─────────────────────────────────────────────────────────
@Composable
fun AddTaskDialog(onConfirm: (String, String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KiranTextField(value = name, onValueChange = { name = it }, label = "Task Name", leadingIcon = Icons.Filled.TaskAlt,
                    isError = name.isBlank(), errorText = "Task name required")
                KiranTextField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)", singleLine = false)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary), enabled = name.isNotBlank()) {
                Text("Add Task")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
