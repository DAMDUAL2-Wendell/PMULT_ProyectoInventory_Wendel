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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBarHome
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.theme.TaskTheme

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
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = taskList, key = { it.id }) { item ->
            taskItem(task = item,
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
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    val cardBackgroundColor by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.tertiaryContainer else
            MaterialTheme.colorScheme.primaryContainer, label = ""
    )

    val cardBackgroundColorCompleted =
        if (task.completado) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.primaryContainer

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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                if(task.completado){
                    Button(onClick = { /*TODO*/ }) {
                        Text(
                            text = "X"
                        )
                    }
                }

                cardTaskName(
                    taskName = task.name,
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                )
                //Spacer(modifier = Modifier.weight(1f))
                iconoTask(imaxeId = task.icono)
            }



            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                    Column {
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
                
                Spacer(modifier = Modifier.weight(1f))

                        completadoCheckbox(task = task, updateTaskCompletion = updateTaskCompletion)

            }
            if (expanded) {
                infoCardTask(
                    key = R.string.repeticionesRealizadas,
                    value = (task.repeticionesRealizadas.toString() + " de " + (task.totalRepeticiones * 3))
                )

                infoCardTask(
                    key = R.string.description,
                    value = task.description
                )

            }

        }
    }
}

@Composable
fun completadoCheckbox(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    modifier:Modifier = Modifier
) {

    Column (
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ) {

        Row {
            Column {
                Text(text = (((task.repeticionesRealizadas.toFloat() / (task.totalRepeticiones * 3).toFloat()) * 100).toInt()).toString() + "%",
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small)))

            }
            Column {
                Text(
                    text = stringResource(R.string.completado)
                )
            }
        }


    }

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


@Composable
fun cardTaskName(
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
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = modifier
                .size(dimensionResource(R.dimen.image_card_size))
                //.padding(dimensionResource(R.dimen.padding_extra_small))
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
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
            updateTaskCompletion = {uno,dos -> {}}
        )
    }
}
