package com.wendelledgar.proyectoinventorywendel.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import com.wendelledgar.proyectoinventorywendel.data.Task
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

/**
 * ViewModel para validar e insertar tasks en la base de datos de room.
 */
class TaskEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    var taskUiState by mutableStateOf(taskUiState())
        private set

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState = taskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    fun changeIconTask(){

        //tasksRepository.updateTask(taskUiState.taskDetails.toTask().copy(icono = ))
    }


    suspend fun saveTask() {
        tasksRepository.insertTask(taskUiState.taskDetails.toTask())
    }

    fun existsTaskByName(name: String):Boolean{
        return tasksRepository.getTaskStreamByName(name) == null
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
    val completed: Boolean? = false,
    val icono: Int? = 2130968577
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
    completado = completed ?: false,
    icono = icono?: 2130968577
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
    completed = completado,
    icono = icono
)
