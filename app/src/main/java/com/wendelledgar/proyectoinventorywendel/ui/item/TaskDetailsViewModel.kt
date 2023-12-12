/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve, update and delete an item from the [TasksRepository]'s data source.
 */
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    val uiState: MutableStateFlow<ItemDetailsUiState> = MutableStateFlow(ItemDetailsUiState())
    init {
        tasksRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                ItemDetailsUiState(outOfStock = it.quantity <= 0, itemDetails = it.toItemDetails())
            }
            .onEach { updatedState ->
                uiState.value = updatedState
            }
            .launchIn(viewModelScope)
    }


    suspend fun reduceQuantityByOne() {
        val currentTask = uiState.value.itemDetails.toItem()
        if(currentTask.quantity > 0) {
            tasksRepository.updateItem(
                currentTask.copy(
                    quantity = currentTask.quantity - 1,
                    seriesRealizadas = currentTask.seriesRealizadas + 1
                )
            )
        }
    }

     suspend fun updateSliderTask(numSlider: Int, numeroRepeticiones: Int){
         val currentTask = uiState.value.itemDetails.toItem()

             val tareaActualizada = when(numSlider) {
                 1 -> currentTask.copy(serie1 = numeroRepeticiones)
                 2 -> currentTask.copy(serie2 = numeroRepeticiones)
                 3 -> currentTask.copy(serie3 = numeroRepeticiones)
                 else -> currentTask
             }

             tasksRepository.updateItem(tareaActualizada)

          uiState.value = uiState.value.copy(itemDetails = tareaActualizada.toItemDetails())
    }

    suspend fun deleteItem() {
        tasksRepository.deleteItem(uiState.value.itemDetails.toItem())
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)