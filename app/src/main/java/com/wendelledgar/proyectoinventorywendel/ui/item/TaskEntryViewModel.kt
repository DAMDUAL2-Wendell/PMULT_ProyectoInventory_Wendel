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
class ItemEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
            //&&seriesRealizadas.isNotBlank()
            //&& quantity.isNotBlank()
        }
    }

    suspend fun saveItem() {
        if (validateInput()) {
            tasksRepository.insertTask(itemUiState.itemDetails.toItem())
        }
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
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

fun ItemDetails.toItem(): Task = Task(
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

fun Task.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Task.toItemDetails(): ItemDetails = ItemDetails(
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
