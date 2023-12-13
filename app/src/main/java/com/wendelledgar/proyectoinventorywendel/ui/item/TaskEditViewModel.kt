package com.wendelledgar.proyectoinventorywendel.ui.item

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

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            itemUiState = tasksRepository.getTaskStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    fun updateSliderTask(numSlider:Int, valor: Int){
        itemUiState = itemUiState.copy(itemDetails = itemUiState.itemDetails.copy(
            serie2 = valor
        ))
    }


    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun updateItem() {
        if (validateInput()) {
            tasksRepository.updateTask(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
                    //&& seriesRealizadas.isNotBlank() && quantity.isNotBlank()
        }
    }
}
