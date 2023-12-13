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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _uiState = MutableStateFlow(ItemUiState())
    val uiState: StateFlow<ItemUiState> get() = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = tasksRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    suspend fun reduceQuantityByOne() {
            val currentTask = _uiState.value.itemDetails.toItem()
            if (currentTask.quantity > 0) {
                tasksRepository.updateItem(
                    currentTask.copy(
                        quantity = currentTask.quantity - 1,
                        seriesRealizadas = currentTask.seriesRealizadas + 1
                    )
                )
                _uiState.value = _uiState.value.copy(itemDetails = currentTask.copy(
                    quantity = currentTask.quantity - 1,
                    seriesRealizadas = currentTask.seriesRealizadas + 1
                ).toItemDetails())
            }
    }
    suspend fun updateSliderTask(numSlider: Int, numeroRepeticiones: Int) {

            val currentTask = _uiState.value.itemDetails.toItem()

            val tareaActualizada = when(numSlider) {
                1 -> currentTask.copy(serie1 = numeroRepeticiones)
                2 -> currentTask.copy(serie2 = numeroRepeticiones)
                3 -> currentTask.copy(serie3 = numeroRepeticiones)
                else -> currentTask
            }

            tasksRepository.updateItem(tareaActualizada)
            _uiState.value = _uiState.value.copy(itemDetails = tareaActualizada.toItemDetails())

    }

    suspend fun deleteItem() {

            tasksRepository.deleteItem(_uiState.value.itemDetails.toItem())
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)
