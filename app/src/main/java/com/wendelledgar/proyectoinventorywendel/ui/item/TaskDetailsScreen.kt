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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import com.wendelledgar.proyectoinventorywendel.bottomAppBarDetail
import com.wendelledgar.proyectoinventorywendel.bottomAppBarEntry
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.TaskTheme
import kotlinx.coroutines.launch

object TaskDetailsDestination : NavigationDestination {
    override val route = "task_details"
    override val titleRes = R.string.task_detail_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navigateToEditTask: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: TaskDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {

    val uiState = viewModel.taskDetailState

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            topAppBar(
                title = stringResource(TaskDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_bar))
            )
        },
        bottomBar = {
            bottomAppBarDetail(
                navigateBack = navigateBack,
                navigateToEdit = { navigateToEditTask(viewModel.taskDetailState.taskDetails.id) }
            )
        },
        /*
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditTask(viewModel.taskDetailState.taskDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_task_title),
                )
            }
        },
        */
        modifier = modifier
    ) { innerPadding ->
        TaskDetailsBody(
            taskDetailsUiState = uiState,
            modificarRepeticiones = {
                coroutineScope.launch {
                    viewModel.modificarRepeticiones(it)
                    viewModel.updateUiState(taskDetails = viewModel.taskDetailState.taskDetails)
                }
            },
            updateUiState = {
                coroutineScope.launch {
                    viewModel.updateUiState(taskDetails = viewModel.taskDetailState.taskDetails)
                }
            },
            updateSliderTask = { numSlider, valor ->
                coroutineScope.launch {
                    viewModel.updateSliderTask(numSlider, valor)
                    viewModel.updateUiState(taskDetails = viewModel.taskDetailState.taskDetails)
                }
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteTask()
                    viewModel.updateUiState(taskDetails = viewModel.taskDetailState.taskDetails)
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
private fun TaskDetailsBody(
    taskDetailsUiState: taskUiState,
    modificarRepeticiones: (Boolean) -> Unit,
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

        taskDetails(
            task = taskDetailsUiState.taskDetails.toTask(),
            updateSliderTask = updateSliderTask,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {modificarRepeticiones(true)},
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = true
        ) {
            Text(stringResource(R.string.aumentar_series))
        }

        Button(
            onClick = {modificarRepeticiones(false)},
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = true
        ) {
            Text(stringResource(R.string.diminuir_series))
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
    sliderNumero: Int,
    totalRepeticiones: Int,
    updateSliderTask: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {


    var sliderValue = sliderValue

    var previousSliderValue by rememberSaveable { mutableStateOf(0f) }


    var sliderCompleted = (sliderValue == totalRepeticiones)

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
        (completed.toFloat()/ total.toFloat())
    } else {
        0f
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        //contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                ,
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                trackColor = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = "$completed/$total",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}


@Composable
fun taskDetails(
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
           
            cardTitle(taskName = task.name)

            TaskDetailsRow(
                labelResID = R.string.seriesNum,
                taskDetail = task.totalRepeticiones.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )

            TaskDetailsRow(
                labelResID = R.string.num_series,
                taskDetail = ((task.totalRepeticiones * 3) - task.repeticionesRealizadas).toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            TaskDetailsRow(
                labelResID = R.string.repeticionesRealizadas,
                taskDetail = task.repeticionesRealizadas.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )

            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie1,
                sliderNumero = 1,
                updateSliderTask = updateSliderTask,
            )

            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie2,
                sliderNumero = 2,
                updateSliderTask = updateSliderTask,
            )

            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie3,
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
fun cardTitle(
    taskName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = taskName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun TaskDetailsRow(
    @StringRes labelResID: Int, taskDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = taskDetail, fontWeight = FontWeight.Bold)
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
fun TaskDetailsScreenPreview() {
    TaskTheme {
        TaskDetailsBody(
            taskUiState(
                taskDetails = TaskDetails(1, "task", "1", "1")
            ),
            modificarRepeticiones = {},
            updateUiState = {},
            updateSliderTask = {uno,dos -> {}},
            onDelete = {}
        )
    }
}
