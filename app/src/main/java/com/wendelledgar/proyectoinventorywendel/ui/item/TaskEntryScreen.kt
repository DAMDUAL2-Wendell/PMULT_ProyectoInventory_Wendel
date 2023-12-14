package com.wendelledgar.proyectoinventorywendel.ui.item

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBarEntry
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.TaskTheme
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
            onSaveClick = {
                coroutineScope.launch {


                    if(!viewModel.existsTaskByName(viewModel.taskUiState.taskDetails.name)){
                        viewModel.saveTask()
                        viewModel.updateUiState(viewModel.taskUiState.taskDetails.copy())
                        navigateBack()

                    }else{
                        Log.d("TaskEntryScreen","la tarea ya existe en la base de datos -> task: ${viewModel.taskUiState.taskDetails.name}")

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
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
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
                text = stringResource(R.string.required_fields),
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
        label = { Text(stringResource(label))},
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

@Preview(showBackground = true)
@Composable
private fun TaskEntryScreenPreview() {
    TaskTheme {
        TaskEntryBody(taskUiState = taskUiState(
            TaskDetails(
                name = "task", seriesRealizadas = "1", quantity = 5
            )
        ), onTaskValueChange = {}, onSaveClick = {})
    }
}
