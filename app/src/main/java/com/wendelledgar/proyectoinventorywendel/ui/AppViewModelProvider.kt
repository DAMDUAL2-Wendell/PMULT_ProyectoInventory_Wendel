package com.wendelledgar.proyectoinventorywendel.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wendelledgar.proyectoinventorywendel.RutinasApplication
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeViewModel
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskDetailsViewModel
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEditViewModel
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer de Ventana editar Tarea
        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.tasksRepository
            )
        }
        // Initializer Ventana nueva Tarea
        initializer {
            TaskEntryViewModel(inventoryApplication().container.tasksRepository)
        }

        // Initializer Ventana Detalles Tarea
        initializer {
            TaskDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.tasksRepository
            )
        }

        // Initializer Ventana Home
        initializer {
            HomeViewModel(inventoryApplication().container.tasksRepository)
        }
    }
}
fun CreationExtras.inventoryApplication(): RutinasApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as RutinasApplication)
