package com.wendelledgar.proyectoinventorywendel.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel para extraer informacion del repository y modificarla.
 */
class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    var taskUiState by mutableStateOf(taskUiState())
        private set

    private val taskId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    init {
        viewModelScope.launch {
            taskUiState = tasksRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .totaskUiState(true)
        }
    }

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState = taskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))

    }

    fun changeIconTask(){
        Log.d("TaskEditViewModel","Entrando en changeIconTask en TaskEditViewModel")
    }

    suspend fun updateTask() {
        if (validateInput()) {
            tasksRepository.updateTask(taskUiState.taskDetails.toTask())
        }
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}
