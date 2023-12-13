package com.wendelledgar.proyectoinventorywendel.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wendelledgar.proyectoinventorywendel.InventoryApplication
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeViewModel
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemDetailsViewModel
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEditViewModel
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemEntryViewModel

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
            ItemEntryViewModel(inventoryApplication().container.tasksRepository)
        }

        // Initializer Ventana Detalles Tarea
        initializer {
            ItemDetailsViewModel(
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
fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
