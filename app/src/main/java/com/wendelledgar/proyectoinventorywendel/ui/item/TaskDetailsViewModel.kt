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
class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val taskId: Int = checkNotNull(savedStateHandle[TaskDetailsDestination.taskIdArg])

    var taskDetailState  by mutableStateOf(taskUiState())
        private set
    //val uiState: StateFlow<taskUiState> get() = taskDetailState

    init {
        viewModelScope.launch {
            taskDetailState = tasksRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .totaskUiState(true)
        }
    }

    suspend fun modificarRepeticiones(boolean: Boolean) {

        Log.d("TaskDetailsViewModel", "Entrando en modificarRepeticiones con valor: $boolean")



        if(boolean){


            val currentTask = taskDetailState.taskDetails.toTask()

            var currentRepeticiones = currentTask.totalRepeticiones

            var repeticionesRealizadas = currentTask.repeticionesRealizadas

            var serie1 = currentTask.serie1

            val updatedTask = currentTask.copy(
                totalRepeticiones = currentRepeticiones +1,
                completado = false,
                repeticionesRealizadas = repeticionesRealizadas
            )

            tasksRepository.updateTask(updatedTask)

            taskDetailState = taskDetailState.copy(taskDetails = updatedTask.totaskDetails())

            Log.d("TaskDetailsViewModel", "suspend fun aumentarRepeticiones -  Repeticiones aumentadas, nueva cantidad: ${updatedTask.totalRepeticiones}")


        }else{


            val currentTask = taskDetailState.taskDetails.toTask()

            var currentRepeticiones = currentTask.totalRepeticiones

            if(currentRepeticiones > 1){



                var serie1 = currentTask.serie1
                if ( serie1 == currentRepeticiones) {
                    serie1 -= 1
                }
                var serie2 = currentTask.serie2
                if(serie2 == currentRepeticiones){
                    serie2 -= 1
                }
                var serie3 = currentTask.serie3
                if(serie3 == currentRepeticiones){
                    serie3 -= 1
                }

                var updatedTask = currentTask.copy(
                    totalRepeticiones = currentRepeticiones - 1,
                    completado = false,
                    serie1 = serie1,
                    serie2 = serie2,
                    serie3 = serie3
                )

                var repeticionesRealizadas = updatedTask.serie1 + updatedTask.serie2 + updatedTask.serie3


                updatedTask = updatedTask.copy(
                    repeticionesRealizadas = repeticionesRealizadas
                )

                tasksRepository.updateTask(updatedTask)

                taskDetailState = taskDetailState.copy(taskDetails = updatedTask.totaskDetails())

                Log.d("TaskDetailsViewModel", "suspend fun aumentarRepeticiones -  Repeticiones aumentadas, nueva cantidad: ${updatedTask.totalRepeticiones}")

            }



        }




    }


    suspend fun cambiarEstadoCompletado(){

        var completado = taskDetailState.taskDetails.toTask().completado

        completado = !completado

        val currentTask = taskDetailState.taskDetails.toTask()
        tasksRepository.updateTask(
            currentTask.copy( completado = completado
            )
        )

        taskDetailState = taskDetailState.copy(taskDetails = currentTask.copy(
            completado = completado
        ).totaskDetails())

    }

    suspend fun updateSliderTask(numSlider: Int, numeroRepeticiones: Int) {

        //Log.d("TaskDetailsViewModel","Entrando en suspend fun updateSliderTask con valores-> numSlider: $numSlider, numeroRepeticiones: $numeroRepeticiones")

            val currentTask = taskDetailState.taskDetails.toTask()

            var tareaActualizada = when(numSlider) {
                1 -> currentTask.copy(serie1 = numeroRepeticiones, repeticionesRealizadas = (numeroRepeticiones + currentTask.serie2 + currentTask.serie3))
                2 -> currentTask.copy(serie2 = numeroRepeticiones, repeticionesRealizadas = (numeroRepeticiones + currentTask.serie1 + currentTask.serie3))
                3 -> currentTask.copy(serie3 = numeroRepeticiones, repeticionesRealizadas = (numeroRepeticiones + currentTask.serie1 + currentTask.serie2))
                else -> currentTask
            }

        if(tareaActualizada.repeticionesRealizadas == tareaActualizada.totalRepeticiones*3){
            tareaActualizada = tareaActualizada.copy(completado = true)
        }else{
            tareaActualizada = tareaActualizada.copy(completado = false)
        }

            taskDetailState = taskDetailState.copy(taskDetails = tareaActualizada.totaskDetails())

        tasksRepository.updateTask(tareaActualizada)

        //Log.d("TaskDetailsViewModel","tarea completada: ${tareaActualizada.completado}")

    }

    fun updateUiState(taskDetails: TaskDetails) {
        taskDetailState =
            taskUiState(taskDetails = taskDetails, isEntryValid = true)
    }

    suspend fun deleteTask() {

            tasksRepository.deletetask(taskDetailState.taskDetails.toTask())
    }
}
