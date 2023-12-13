package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    var taskDetailState  by mutableStateOf(ItemUiState())
        private set
    //val uiState: StateFlow<ItemUiState> get() = taskDetailState

    init {
        viewModelScope.launch {
            taskDetailState = tasksRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    suspend fun reduceQuantityByOne() {
            val currentTask = taskDetailState.itemDetails.toItem()
            if (currentTask.totalRepeticiones > 0 && currentTask.repeticionesRealizadas < (currentTask.totalRepeticiones * 3)) {
                tasksRepository.updateItem(
                    currentTask.copy(
                        //quantity = currentTask.quantity - 1,
                        repeticionesRealizadas = currentTask.repeticionesRealizadas + 1
                    )
                )

                taskDetailState = taskDetailState.copy(itemDetails = currentTask.copy(
                    //quantity = currentTask.quantity - 1,
                    repeticionesRealizadas = currentTask.repeticionesRealizadas + 1
                ).toItemDetails())

            }
    }
    suspend fun updateSliderTask(numSlider: Int, numeroRepeticiones: Int) {

            val currentTask = taskDetailState.itemDetails.toItem()

            val tareaActualizada = when(numSlider) {
                1 -> currentTask.copy(serie1 = numeroRepeticiones, repeticionesRealizadas = (numeroRepeticiones + currentTask.serie2 + currentTask.serie3))
                2 -> currentTask.copy(serie2 = numeroRepeticiones, repeticionesRealizadas = (numeroRepeticiones + currentTask.serie1 + currentTask.serie3))
                3 -> currentTask.copy(serie3 = numeroRepeticiones, repeticionesRealizadas = (numeroRepeticiones + currentTask.serie1 + currentTask.serie2))
                else -> currentTask
            }

            tasksRepository.updateItem(tareaActualizada)

            taskDetailState = taskDetailState.copy(itemDetails = tareaActualizada.toItemDetails())

    }

    fun updateUiState(itemDetails: ItemDetails) {
        taskDetailState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = true)
    }

    suspend fun deleteItem() {

            tasksRepository.deleteItem(taskDetailState.itemDetails.toItem())
    }
}

data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)
