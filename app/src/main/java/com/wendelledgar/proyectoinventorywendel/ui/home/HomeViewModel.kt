package com.wendelledgar.proyectoinventorywendel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.ui.item.toTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateTaskComplete(taskId: Int, completed: Boolean){
            if(completed){

                viewModelScope.launch {
                    val repes = tasksRepository.getTaskStream(taskId).filterNotNull().first().totalRepeticiones
                    val task = tasksRepository.getTaskStream(taskId)
                        .filterNotNull()
                        .first()
                        .let { task ->
                            tasksRepository.updateTask(
                                task.copy(
                                    completado = completed,
                                    totalRepeticiones = repes,
                                    serie1 = repes,
                                    serie2 = repes,
                                    serie3 = repes,
                                    repeticionesRealizadas = (repes * 3)
                                )
                            )
                        }
                }

            }else {
                viewModelScope.launch {
                    val task = tasksRepository.getTaskStream(taskId)
                        .filterNotNull()
                        .first()
                        .let { task ->
                            tasksRepository.updateTask(
                                task.copy(
                                    completado = completed,
                                    serie1 = 0,
                                    serie2 = 0,
                                    serie3 = 0,
                                    repeticionesRealizadas = 0
                                )
                            )
                        }
                }

            }
    }

    suspend fun deleteTask(id: Int) {
     tasksRepository.deletetask(tasksRepository.getTaskStream(id).filterNotNull().first());
    }
    

    val homeUiState: StateFlow<HomeUiState> =
        tasksRepository.getAllTasksStream()
            .map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

}

data class HomeUiState(val taskList: List<Task> = listOf())


