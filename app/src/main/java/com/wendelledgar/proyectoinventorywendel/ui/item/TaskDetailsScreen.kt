package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBarDetail
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
    navigateToHome: () -> Unit,
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
                navigateToHome = navigateToHome,
                navigateToEdit = { navigateToEditTask(viewModel.taskDetailState.taskDetails.id) }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        TaskDetailsBodyButtons(
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

/**
 * Botones con funcionalidades sobre una task (aumentar o diminuir repeticiones, eliminar task).
 */
@Composable
private fun TaskDetailsBodyButtons(
    taskDetailsUiState: taskUiState,
    modificarRepeticiones: (Boolean) -> Unit,
    updateUiState: () -> Unit,
    updateSliderTask: (Int, Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        // Padding alrededor, aplicado a toda la Task.
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        // Detalles de una task, título, sliders, etc.
        taskDetails(
            task = taskDetailsUiState.taskDetails.toTask(),
            updateSliderTask = updateSliderTask,
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
        )
        {
            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .padding(0.dp)
            ) {
                // Boton aumentar repeticiones, aparece a la derecha
                Button(
                    onClick = {modificarRepeticiones(false)},
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(0.dp),
                    enabled = true
                ) {
                    Text(
                        text = stringResource(R.string.diminuir_series),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1, // Texto en una sola linea
                        overflow = TextOverflow.Ellipsis, // si el texto es muy largo agrega puntos suspensivos
                        softWrap = true // permite que el texto se ajuste automáticamente al tamaño disponible
                    )
                }
            }
            // Separador horizontal
            Spacer(modifier = Modifier.weight(0.1f))

            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .padding(0.dp)
            ) {
                // Boton diminuir repeticiones, a la izquierda
                Button(
                    onClick = {modificarRepeticiones(true)},
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                    shape = MaterialTheme.shapes.small,
                    enabled = true
                ) {
                    Text(
                        text = stringResource(R.string.aumentar_series),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1, // Texto en una sola linea
                        overflow = TextOverflow.Ellipsis, // si el texto es muy largo agrega puntos suspensivos
                        softWrap = true // permite que el texto se ajuste automáticamente al tamaño disponible
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                .padding(top = 0.dp, bottom = 0.dp)
        ) {
            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
        }

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                title = R.string.attention,
                text = R.string.delete_question,
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

/**
 * Box con una Row, muestra un slider de la task y un checkBox para completar el slider con las repeticiones.
 */
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
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_medium),
                end = 8.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sliderValue?.toString() ?: "0",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
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
                modifier = Modifier.weight(8f)
            )
            Checkbox(
                checked = sliderCompleted,
                modifier = Modifier.weight(1.5f),
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


/**
 * ProgressBar que muestra el número de repeticiones realizadas sobre el total de la task.
 */
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
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        color = MaterialTheme.colorScheme.primaryContainer,
        //contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                    //.border(0.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(0.dp)),
                strokeCap = StrokeCap.Round,
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

/**
 * Detalles de una Task, título, sliders, etc
 */
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
                .padding(0.dp),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {

            cardTitle(taskName = task.name)

            // Row que muestra el numero de repeticiones de una serie
            TaskDetailsRow(
                labelResID = R.string.seriesNum,
                taskDetail = task.totalRepeticiones.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )

            // Row con el numero de repeticiones restantes
            TaskDetailsRow(
                labelResID = R.string.num_series,
                taskDetail = ((task.totalRepeticiones * 3) - task.repeticionesRealizadas).toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            // Row con el numero de repeticiones realizadas
            TaskDetailsRow(
                labelResID = R.string.repeticionesRealizadas,
                taskDetail = task.repeticionesRealizadas.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )

            // Primer slider
            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie1,
                sliderNumero = 1,
                updateSliderTask = updateSliderTask
            )

            // Segundo slider
            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie2,
                sliderNumero = 2,
                updateSliderTask = updateSliderTask,
            )

            // Tercer Slider
            sliderAndCheckbox(
                totalRepeticiones = task.totalRepeticiones,
                sliderValue = task.serie3,
                sliderNumero = 3,
                updateSliderTask = updateSliderTask,
            )

            // ProgressBar con la cantidad de repeticiones realizadas sobre el total
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
public fun DeleteConfirmationDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(text)) },
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
        TaskDetailsBodyButtons(
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
