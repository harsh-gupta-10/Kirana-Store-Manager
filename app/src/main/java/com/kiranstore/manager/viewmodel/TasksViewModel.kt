package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

data class Task(
    val id: Long,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@HiltViewModel
class TasksViewModel @Inject constructor() : ViewModel() {

    private val idCounter = AtomicLong(0)

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(title: String) {
        if (title.isBlank()) return
        _tasks.value = _tasks.value + Task(id = idCounter.incrementAndGet(), title = title.trim())
    }

    fun toggleTask(taskId: Long) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun deleteTask(taskId: Long) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }
}
