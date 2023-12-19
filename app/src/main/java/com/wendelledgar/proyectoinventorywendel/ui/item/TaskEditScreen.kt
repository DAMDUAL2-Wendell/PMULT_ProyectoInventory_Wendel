package com.wendelledgar.proyectoinventorywendel.ui.item

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBarEntry
import com.wendelledgar.proyectoinventorywendel.data.TasksRepository
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeDestination
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.TaskTheme
import kotlinx.coroutines.launch

object TaskEditDestination : NavigationDestination {
    override val route = "task_edit"
    override val titleRes = R.string.edit_task_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            topAppBar(
                title = stringResource(TaskEditDestination.titleRes),
                canNavigateBack = true,
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
        modifier = modifier
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            changeIconTask = {viewModel.changeIconTask()},
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTask()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TaskEditScreenPreview() {
    TaskTheme {
        Scaffold(
            topBar = {
                topAppBar(
                    title = stringResource(TaskEditDestination.titleRes),
                    canNavigateBack = true,
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
            modifier = Modifier
        ) {
            TaskEntryBody(
                taskUiState = taskUiState(TaskDetails()),
                onTaskValueChange = {},
                changeIconTask = {},
                onSaveClick = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}

data class taskDetails(
    val name: String = "",
    val description: String = "",
    val quantity: Int? = null,
    val icono: Int? = null
)

data class TaskUiState(val taskDetails: TaskDetails) {
}



 */