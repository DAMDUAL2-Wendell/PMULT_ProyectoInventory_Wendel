package com.wendelledgar.proyectoinventorywendel.ui.item

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBarEntry
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.home.iconoTask
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.TaskTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.task_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateHome: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            topAppBar(
                title = stringResource(TaskEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_bar))
            )
        },
        bottomBar = {
            bottomAppBarEntry(
                navigateToHome = navigateHome,
                navigateToEdit = onNavigateUp
            )
        },
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            changeIconTask = { viewModel.changeIconTask() },
            onSaveClick = {
                coroutineScope.launch {


                    if (!viewModel.existsTaskByName(viewModel.taskUiState.taskDetails.name)) {
                        viewModel.saveTask()
                        viewModel.updateUiState(viewModel.taskUiState.taskDetails.copy())
                        navigateBack()

                    } else {
                        Log.d(
                            "TaskEntryScreen",
                            "la tarea ya existe en la base de datos -> task: ${viewModel.taskUiState.taskDetails.name}"
                        )

                        /**
                         *
                         * Implementar mensaje en View para cuando ya hay una tarea con el mismo nombre.
                         *
                         *
                         *
                         */

                    }


                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: taskUiState,
    onTaskValueChange: (TaskDetails) -> Unit,
    changeIconTask: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        // Inputs para modificar una task
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Button(
                onClick = changeIconTask,
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {

                /**
                 *
                 * Falta implementar logica para seleccionar un icono de una lista y asignarlo a la task.
                 *
                 */
                iconoTask(taskUiState.taskDetails.icono ?: 2130968577)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        // Boton guardar
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSaveClick,
                enabled = taskUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
            ) {
                Text(text = stringResource(R.string.save_action))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {

        var titulo = taskDetails.name.isNotBlank()

        outliedTextField(
            value = taskDetails.name,
            label = R.string.task_name_req,
            onValueChange = { onValueChange(taskDetails.copy(name = it)) },
            keyboardType = KeyboardType.Text,
        )

        outliedTextField(
            value = taskDetails.description,
            label = R.string.description,
            onValueChange = { onValueChange(taskDetails.copy(description = it)) },
            keyboardType = KeyboardType.Text,
        )

        outliedTextField(
            value = taskDetails.quantity,
            label = R.string.cantidad_series,
            onValueChange = { onValueChange(taskDetails.copy(quantity = it?.toIntOrNull())) },
            keyboardType = KeyboardType.Decimal,
        )

        if (!titulo) {
            Text(
                text = stringResource(R.string.campos_requeridos),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}


@Composable
fun outliedTextField(
    value: Any?,
    @StringRes label: Int,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { onValueChange(it) },
        label = { Text(stringResource(label)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TaskEntryScreenPreview() {
    val tasksRepository: TasksRepository = FakeTasksRepository()

    val viewModel = TaskEntryViewModel(tasksRepository)

    val taskUiState by rememberUpdatedState(viewModel.taskUiState)

    TaskTheme {
        Scaffold(
            topBar = {
                topAppBar(
                    title = stringResource(TaskEntryDestination.titleRes),
                    canNavigateBack = false,
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                    modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_bar))
                )
            },
            bottomBar = {
                bottomAppBarEntry(
                    navigateToHome = {},
                    navigateToEdit = {}
                )
            },
        ) {
            TaskEntryBody(
                taskUiState = taskUiState,
                onTaskValueChange = {},
                changeIconTask = {},
                onSaveClick = {},
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
        }
    }
}

class FakeTasksRepository : TasksRepository {
    private val tasks = mutableListOf<Task>()
    override fun getAllTasksStream(): Flow<List<Task>> {TODO()}
    override fun getTaskStream(id: Int): Flow<Task?> {TODO()}
    override fun getTaskStreamByName(name: String): Flow<Task?> {TODO()}
    override suspend fun insertTask(task: Task) {TODO()}
    override suspend fun insertListTasks(list: List<Task>) {TODO()}
    override suspend fun deletetask(task: Task) {TODO()}
    override suspend fun updateTask(task: Task) {TODO()}
}

