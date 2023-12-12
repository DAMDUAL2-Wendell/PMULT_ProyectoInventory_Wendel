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

package com.wendelledgar.proyectoinventorywendel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import com.wendelledgar.proyectoinventorywendel.data.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateTaskComplete(taskId: Int, completed: Boolean){
        viewModelScope.launch {
            val task = tasksRepository.getItemStream(taskId)
                .filterNotNull()
                .first()
                .let {task -> tasksRepository.updateItem(task.copy(completado = completed))}
        }
    }


    // Non definimos _homeUiState porque este StateFlow non e Mutable
    val homeUiState: StateFlow<HomeUiState> =
        tasksRepository.getAllItemsStream()
            .map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
    /** stateIn para convertir un Flow en StateFlow, que e a forma recomendada de exponher un Flow
     * dende un ViewModel. Os argumentos son:
     * - scope: cando se cancela o scope, cancelase o StateFlow. Os fluxos tenhen que cancelarse cando
     * non quedan observadores, no fin do ciclo de vida do componible
     * - started: a canalizacion solo esta activa cando a UI e visible, SharingStarted.WhileSubscribed().
     * Ademais, configurase unha demora entre a desaparacion do ultimo suscritor e a detencion da corrutina
     * de uso compartido. Por exemplo cando xira a pantalla desaparece a suscricion por un instante, pero
     * asi non se deten a corrutina (o ViewModel sobrevive aos cambios de configuracion, a pantalla non).
     * - initialValue: valor inicial do fluxo de estado
     */
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val taskList: List<Task> = listOf())


