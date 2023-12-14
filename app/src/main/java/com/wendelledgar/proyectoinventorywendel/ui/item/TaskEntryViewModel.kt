package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import com.wendelledgar.proyectoinventorywendel.data.Task

/**
 * ViewModel para validar e insertar tasks en la base de datos de room.
 */
class TaskEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    var taskUiState by mutableStateOf(taskUiState())
        private set

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            taskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            tasksRepository.insertTask(taskUiState.taskDetails.toTask())
        }
    }
}

data class taskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val seriesRealizadas: String = "",
    val quantity: Int? = 15,
    val serie1: Int? = 0,
    val serie2: Int? = 0,
    val serie3: Int? = 0,
    val completed: Boolean? = false
)

fun TaskDetails.toTask(): Task = Task(
    id = id,
    name = name,
    description = description,
    repeticionesRealizadas = seriesRealizadas.toIntOrNull() ?: 0,
    totalRepeticiones = quantity ?: 15,
    serie1 = serie1 ?: 0,
    serie2 = serie2 ?: 0,
    serie3 = serie3 ?: 0,
    completado = completed ?: false
)

fun Task.totaskUiState(isEntryValid: Boolean = false): taskUiState = taskUiState(
    taskDetails = this.totaskDetails(),
    isEntryValid = isEntryValid
)

fun Task.totaskDetails(): TaskDetails = TaskDetails(
    id = id,
    name = name,
    description = description,
    seriesRealizadas = repeticionesRealizadas.toString(),
    quantity = totalRepeticiones,
    serie1 = serie1,
    serie2 = serie2,
    serie3 = serie3,
    completed = completado
)
