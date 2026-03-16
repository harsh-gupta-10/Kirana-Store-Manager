package com.kiranstore.manager.ui.screens.tasks

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
import com.kiranstore.manager.viewmodel.Task
import com.kiranstore.manager.viewmodel.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val pendingTasks = tasks.filter { !it.isCompleted }
    val completedTasks = tasks.filter { it.isCompleted }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tasks", fontWeight = FontWeight.Bold)
                        Text("आज के काम", fontSize = 12.sp, color = TextSecondary)
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
                Icon(Icons.Filled.Add, contentDescription = "Add Task",
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
                            Text("PENDING", fontSize = 10.sp, color = TextSecondary,
                                fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("${pendingTasks.size}", fontSize = 24.sp,
                                fontWeight = FontWeight.Bold, color = OrangePrimary)
                            Text("बाकी काम", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("COMPLETED", fontSize = 10.sp, color = TextSecondary,
                                fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("${completedTasks.size}", fontSize = 24.sp,
                                fontWeight = FontWeight.Bold, color = GreenSuccess)
                            Text("पूरे हुए काम", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }

            // Pending Tasks Section
            if (pendingTasks.isNotEmpty()) {
                item {
                    Text(
                        "PENDING TASKS",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = TextSecondary, letterSpacing = 1.sp
                    )
                }
                items(pendingTasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = { viewModel.toggleTask(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            // Completed Tasks Section
            if (completedTasks.isNotEmpty()) {
                item {
                    Text(
                        "COMPLETED",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = TextSecondary, letterSpacing = 1.sp
                    )
                }
                items(completedTasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = { viewModel.toggleTask(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            // Empty State
            if (tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.ChecklistRtl, contentDescription = null,
                                tint = TextSecondary, modifier = Modifier.size(56.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("No tasks yet", fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp, color = TextPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text("Tap + to add your first task",
                                fontSize = 14.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }

    // Add Task Dialog
    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title ->
                viewModel.addTask(title)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (task.isCompleted) GreenLight else CardWhite,
        label = "taskBg"
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
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = GreenSuccess,
                    uncheckedColor = OrangePrimary
                ),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                color = if (task.isCompleted) TextSecondary else TextPrimary,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete",
                    tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Task?") },
            text = { Text("Are you sure you want to delete \"${task.title}\"?") },
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
private fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Add Task", fontWeight = FontWeight.Bold)
                Text("नया काम जोड़ें", fontSize = 12.sp, color = TextSecondary)
            }
        },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; error = false },
                label = { Text("Task") },
                placeholder = { Text("e.g. Restock dal shelf") },
                isError = error,
                supportingText = { if (error) Text("Task name is required") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) error = true
                    else onAdd(title)
                },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Task", fontWeight = FontWeight.Bold)
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
