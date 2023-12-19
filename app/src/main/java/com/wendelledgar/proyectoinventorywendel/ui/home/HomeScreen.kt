package com.wendelledgar.proyectoinventorywendel.ui.home

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBarHome
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.item.DeleteConfirmationDialog
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.TaskTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val homeUiState by viewModel.homeUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
        bottomBar = {
                    bottomAppBarHome(
                        onClickAddTask = navigateToTaskEntry,
                    )
        },
        topBar = {
            topAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_bar))
            )
        },
    ) { innerPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            updateTaskCompletion = viewModel::updateTaskComplete,
            onTaskClick = navigateToTaskUpdate,
            deleteTask = { id -> coroutineScope.launch { viewModel.deleteTask(id) } },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    onTaskClick: (Int) -> Unit,
    deleteTask: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (taskList.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.no_task_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            InventoryList(
                taskList = taskList,
                updateTaskCompletion = updateTaskCompletion,
                deleteTask = deleteTask,
                onTaskClick = { onTaskClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun InventoryList(
    taskList: List<Task>,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    deleteTask: (Int) -> Unit,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {
        items(items = taskList, key = { it.id }) { item ->
            taskItem(task = item,
                deleteTask = deleteTask,
                updateTaskCompletion = updateTaskCompletion,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_mini))
                    .clickable { onTaskClick(item) })
        }
    }
}

@Composable
private fun taskItem(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    deleteTask: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by rememberSaveable { mutableStateOf(false) }


    val cardBackgroundColor by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.tertiaryContainer else
            MaterialTheme.colorScheme.primaryContainer, label = ""
    )

    val cardBackgroundColorCompleted =
        if (task.completado) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.primaryContainer

    // Card con el contenido que queremos mostrar de una task
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .background(
                    cardBackgroundColorCompleted
                )
                .padding(dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {

            // Fila con información de una task mostrando el título y un icono.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                // Columna con un icono
                iconoTask(
                    imaxeId = task.icono,
                    modifier = Modifier
                        .weight(0.15f)
                        .fillMaxSize()
                )

                // No me gusta el boton aquí
                //botonEliminarConConfirmacion(taskId = task.id, deleteTask = deleteTask, taskCompleted = task.completado)

                //Spacer(modifier = Modifier.weight(0.1f))

                // Columna con el titulo de una Task
                columnaConText(
                    taskName = task.name,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(0.8f)
                        //.padding(dimensionResource(id = R.dimen.padding_medium))
                        .fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.End
                        //.padding(top = dimensionResource(id = R.dimen.padding_small))
                ) {
                    progressBar(
                        total = task.totalRepeticiones * 3,
                        completed = task.repeticionesRealizadas,
                        taskCompleted = task.completado
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Columna con Expand button
                    Column(
                        modifier = Modifier
                            .weight(0.1f)
                            .padding(
                                start = dimensionResource(id = R.dimen.padding_mini),
                                top = dimensionResource(id = R.dimen.padding_small)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                                .size(dimensionResource(id = R.dimen.boton_extender))
                        ) {
                            expandButton(
                                expanded = expanded,
                                onClick = { expanded = !expanded }
                            )
                        }
                    }
                // Espacio horizontal
                //Spacer(modifier = Modifier.weight(1f))

                /*

                // Columna con el ProgressBar no me convence aquí, pondré uno redonddo arriba
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(top = dimensionResource(id = R.dimen.padding_small))
                ) {
                    progressBar(
                        total = task.totalRepeticiones * 3,
                        completed = task.repeticionesRealizadas,
                        taskCompleted = task.completado
                    )
                }

                 */

                // Switch boton completado
                columnaSwitchCompletado(task = task, updateTaskCompletion = updateTaskCompletion, modifier = modifier.weight(0.2f))

            }
            // Si pulsamos boton expandir mostramos info con repeticiones y descripcion de una task
            if (expanded) {
                // Row con info de repeticiones realizadas, no me gusta, de momento lo quito, he puesto un progressBar entonces esto ya no sería necesario
                /*
                infoCardTask(
                    key = R.string.repeticionesRealizadas,
                    value = (task.repeticionesRealizadas.toString() + " de " + (task.totalRepeticiones * 3))
                )*/
                infoDescripcion(
                    key = R.string.description,
                    value = task.description
                )
            }
        }
    }
}

// Columna con un switch para completar una tarea
@Composable
fun columnaSwitchCompletado(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    modifier:Modifier = Modifier
) {
    // No me gusta demasiado así, lo cambio por un slider.
    //columnaPorcentajeCompletado(task)

    // Slider para ver el progreso total en un task

    // No me gusta como queda el slider, pondre una ProgressBar
    //sliderAndCheckbox(task = task)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {

        Spacer(Modifier.weight(1f))

        switchCompleted(
            task = task,
            updateTaskCompletion = updateTaskCompletion
        )
    }
}


/**
 * Función componible que muestra una ProgressBar con el porcentaje completado sobre el total de los sliders.
 */
@Composable
fun progressBar(
    total: Int,
    completed: Int,
    taskCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = if (total > 0) {
        (completed.toFloat()/ total.toFloat())
    } else {
        0f
    }

    val surfaceColor =
        if (taskCompleted) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.primaryContainer

    val trackColor =
        if (taskCompleted) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.tertiary


    Surface(
        modifier = Modifier,
        color = surfaceColor,
        //contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.circular_progress_size))
                    //.padding(end = 2.dp)
                    .background(
                        color = surfaceColor,
                        shape = MaterialTheme.shapes.small
                    ),
                trackColor = trackColor
            )

            Text(
                text = "$completed/$total",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@Composable
fun sliderAndCheckbox(
    task: Task,
    modifier: Modifier = Modifier
) {
    var sliderValue by rememberSaveable{ mutableStateOf(0f) }
    sliderValue = (task.repeticionesRealizadas).toFloat()

    var totalRepeticiones by remember{ mutableStateOf(0) }
    totalRepeticiones = (task.totalRepeticiones * 3) ?: 0

    Column(
        modifier = modifier
    ) {
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
                    onValueChange = {},
                    valueRange = 0f..totalRepeticiones.toFloat(),
                    steps = (totalRepeticiones),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }


}


@Composable
fun columnaPorcentajeCompletado(
    task: Task,
    modifier: Modifier = Modifier
) {
    // Columna con un porcentaje y un texto;    Ejemplo: '100% Completado'
    Column (
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Row {
            // Columna con texto que marca porcentaje completado, por ejemplo 100%
            Column {
                Text(text = (((task.repeticionesRealizadas.toFloat() / (task.totalRepeticiones * 3).toFloat()) * 100).toInt()).toString() + "%",
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small)))
            }
            // Columna con el texto Completado
            Column {
                Text(
                    text = stringResource(R.string.completado)
                )
            }
        }
    }
}


@Composable
fun botonEliminarConConfirmacion(
    taskCompleted: Boolean,
    taskId: Int,
    deleteTask: (Int) -> Unit
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    if(taskCompleted){
        Button(onClick = { deleteConfirmationRequired = true}) {
            Text(
                text = "X"
            )
        }
    }
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            title = R.string.attention,
            text = R.string.delete_question,
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                deleteTask(taskId)
            },
            onDeleteCancel = { deleteConfirmationRequired = false },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
fun columnaConText(
    taskName: String,
    fontSize: TextUnit = 30.sp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = taskName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    }
}

@Composable
fun infoDescripcion(
    @StringRes key: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 0.dp,
                start = dimensionResource(id = R.dimen.padding_small)
            )
    ) {
            Text(
                text = stringResource(id = key),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_extra_small)
            )
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}



@Composable
fun infoCardTask(
    @StringRes key: Int,
    value: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Column {
            Text(
                text = stringResource(id = key),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))

        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }


    }
}

@Composable
fun iconoTask(
    @DrawableRes imaxeId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                //.size(dimensionResource(R.dimen.image_card_size))
                .padding(0.dp)
                //.padding(dimensionResource(R.dimen.padding_extra_small))
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Inside,
            painter = painterResource(imaxeId),
            contentDescription = null
        )
    }

}

@Composable
fun expandButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun switchCompleted(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
) {
    var checked = task.completado
    
    Switch(
        checked = checked,
        onCheckedChange = { checkChange ->
            checked = checkChange
            updateTaskCompletion(task.id,checkChange)
        },
        modifier = Modifier.padding(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    TaskTheme {
        HomeBody(listOf(
            Task(1, "task", "", 20),
            Task(2, "task2", "", 30)
        ),
            onTaskClick = {},
            deleteTask = {},
            updateTaskCompletion = {uno,dos -> {}}
        )
    }
}
