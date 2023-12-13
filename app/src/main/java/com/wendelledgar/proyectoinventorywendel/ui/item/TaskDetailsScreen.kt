package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {

    val uiState = viewModel.taskDetailState


    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            topAppBar(
                title = stringResource(ItemDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(viewModel.taskDetailState.itemDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        ItemDetailsBody(
            itemDetailsUiState = uiState,
            onTerminarSerie = {
                coroutineScope.launch {
                    viewModel.reduceQuantityByOne()
                    viewModel.updateUiState(itemDetails =  viewModel.taskDetailState.itemDetails)
                }
            },
            updateUiState = {
                coroutineScope.launch {
                    viewModel.updateUiState(itemDetails =  viewModel.taskDetailState.itemDetails)
                }
            },
            updateSliderTask = { numSlider, valor ->
                coroutineScope.launch {
                    viewModel.updateSliderTask(numSlider, valor)
                    viewModel.updateUiState(itemDetails =  viewModel.taskDetailState.itemDetails)
                }
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem()
                    viewModel.updateUiState(itemDetails =  viewModel.taskDetailState.itemDetails)
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun ItemDetailsBody(
    itemDetailsUiState: ItemUiState,
    onTerminarSerie: () -> Unit,
    updateUiState: () -> Unit,
    updateSliderTask: (Int, Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        ItemDetails(
            task = itemDetailsUiState.itemDetails.toItem(),
            updateSliderTask = updateSliderTask,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onTerminarSerie,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = true
        ) {
            Text(stringResource(R.string.btn_terminar_serie))
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun sliderAndCheckbox(
    sliderValue: Int,
    completed: Boolean,
    sliderNumero: Int,
    totalRepeticiones: Int,
    updateSliderTask: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    //var sliderValue by rememberSaveable { mutableStateOf(sliderValue) }

    var sliderValue = sliderValue

    var previousSliderValue by rememberSaveable { mutableStateOf(0f) }


    var sliderCompleted by rememberSaveable { mutableStateOf(completed) }

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
                text = sliderValue?.toString() ?: "0",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Slider(
                value = sliderValue.toFloat(),
                onValueChange = {
                        sliderValue = it.toInt()
                        updateSliderTask(sliderNumero, it.toInt())
                        if(sliderValue.toInt() == totalRepeticiones) sliderCompleted = true else sliderCompleted = false
                },
                valueRange = 0f..totalRepeticiones.toFloat(),
                steps = (totalRepeticiones),
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = sliderCompleted,
                onCheckedChange = {
                    sliderCompleted = it
                    if (it) {
                        previousSliderValue = sliderValue?.toFloat() ?: 0f
                        sliderValue = totalRepeticiones
                        updateSliderTask(sliderNumero, sliderValue)
                    } else {
                        sliderValue = previousSliderValue.toInt()
                        updateSliderTask(sliderNumero, previousSliderValue.toInt())
                    }

                }
            )
        }
    }
}



@Composable
fun progressBar(
    total: Int,
    completed: Int
) {
    val progress = if (total > 0) {
        (total.toFloat() - completed.toFloat())
    } else {
        0f
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                trackColor = MaterialTheme.colorScheme.inversePrimary
            )

            Text(
                text = "$completed/$total",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}


@Composable
fun ItemDetails(
    task: Task,
    updateSliderTask: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            ItemDetailsRow(
                labelResID = R.string.item,
                itemDetail = task.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            ItemDetailsRow(
                labelResID = R.string.num_series,
                itemDetail = ((task.serie1 + task.serie2 + task.serie3) - task.repeticionesRealizadas).toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            ItemDetailsRow(
                labelResID = R.string.repeticionesRealizadas,
                itemDetail = task.repeticionesRealizadas.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )

            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie1,
                completed = task.completado,
                sliderNumero = 1,
                updateSliderTask = updateSliderTask,
            )

            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie2,
                completed = task.completado,
                sliderNumero = 2,
                updateSliderTask = updateSliderTask,
            )

            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie3,
                completed = task.completado,
                sliderNumero = 3,
                updateSliderTask = updateSliderTask,
            )

            progressBar(
                total = (task.totalRepeticiones * 3),
                completed = (task.serie1 + task.serie2 + task.serie3)
            )

        }
    }
}

@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    InventoryTheme {
        ItemDetailsBody(
            ItemUiState(
                itemDetails = ItemDetails(1, "Pen", "100", "10")
            ),
            onTerminarSerie = {},
            updateUiState = {},
            updateSliderTask = {uno,dos -> {}},
            onDelete = {}
        )
    }
}
