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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.InventoryTopAppBar
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.data.seriesPorDefecto
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object ItemEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ItemEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()  // Asegura que el contenido ocupe todo el espacio disponible
                .padding(innerPadding)
        ) {
            ItemEntryBody(
                itemUiState = viewModel.itemUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateItem()
                        navigateBack()
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
            sliders2(task = viewModel.itemUiState.itemDetails.toItem(), updateSliderTask = viewModel::updateSliderTask)

        }
    }
}

@Composable
fun sliders2(
    task: Task,
    updateSliderTask: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    var slider2 by rememberSaveable { mutableStateOf(task.serie2) }
    var isCheckedSlider2 by rememberSaveable { mutableStateOf(false) }
    var previousSlider2Value by rememberSaveable { mutableStateOf(0f) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = slider2?.toString() ?: "0",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Slider(
                value = if (isCheckedSlider2) seriesPorDefecto.numeroRepeticiones.toFloat() else slider2?.toFloat() ?: 0f,
                onValueChange = {
                    if (!isCheckedSlider2) {
                        slider2 = it.toInt()
                        updateSliderTask(2, it.toInt())
                    }
                },
                valueRange = 0f..seriesPorDefecto.numeroRepeticiones.toFloat(),
                steps = (seriesPorDefecto.numeroRepeticiones + 1).toInt(),
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = isCheckedSlider2,
                onCheckedChange = {
                    isCheckedSlider2 = it
                    if (it) {
                        previousSlider2Value = slider2?.toFloat() ?: 0f
                        slider2 = seriesPorDefecto.numeroRepeticiones.toInt()
                        updateSliderTask(2, seriesPorDefecto.numeroRepeticiones)
                    } else {
                        slider2 = previousSlider2Value.toInt()
                        updateSliderTask(2, previousSlider2Value.toInt())
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    InventoryTheme {
        ItemEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditBodyPreview() {
    InventoryTheme {
        ItemEntryBody(itemUiState = ItemUiState(
            ItemDetails(
                name = "Item name", seriesRealizadas = "10", quantity = "5"
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}

