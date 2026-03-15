package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.database.entities.TaskEntity
import com.kiranstore.manager.data.repository.TaskRepository
import com.kiranstore.manager.utils.todayEndMs
import com.kiranstore.manager.utils.todayStartMs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUiState(
    val shopId: Long = 0L,
    val todayTasks: List<TaskEntity> = emptyList(),
    val isAddDialogOpen: Boolean = false
)

@HiltViewModel
class TaskViewModel @Inject constructor(private val repo: TaskRepository) : ViewModel() {

    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state.asStateFlow()

    fun init(shopId: Long) {
        _state.update { it.copy(shopId = shopId) }
        viewModelScope.launch {
            repo.getTasksForDay(shopId, todayStartMs(), todayEndMs()).collect { tasks ->
                _state.update { it.copy(todayTasks = tasks) }
            }
        }
    }

    fun addTask(name: String, notes: String) {
        val shopId = _state.value.shopId
        viewModelScope.launch { repo.addTask(TaskEntity(shopId = shopId, name = name, notes = notes)) }
    }

    fun markAsDone(taskId: Long) = viewModelScope.launch { repo.markAsDone(taskId) }
    fun deleteTask(task: TaskEntity) = viewModelScope.launch { repo.deleteTask(task) }
    fun openAddDialog() = _state.update { it.copy(isAddDialogOpen = true) }
    fun closeAddDialog() = _state.update { it.copy(isAddDialogOpen = false) }
}
