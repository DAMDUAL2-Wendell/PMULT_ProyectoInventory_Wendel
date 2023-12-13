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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.bottomAppBar
import com.wendelledgar.proyectoinventorywendel.data.Task
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ,
        //containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
                    bottomAppBar(
                        onClickAddItem = navigateToItemEntry,
                    )
        },
        topBar = {
            /*
            InventoryTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )*/
            topAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_bar))
            )
        },
/*
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },*/

    ) { innerPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            updateTaskCompletion = viewModel::updateTaskComplete,
            onItemClick = navigateToItemUpdate,
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
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (taskList.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            InventoryList(
                taskList = taskList,
                updateTaskCompletion = updateTaskCompletion,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun InventoryList(
    taskList: List<Task>,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    onItemClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = taskList, key = { it.id }) { item ->
            InventoryItem(task = item,
                updateTaskCompletion = updateTaskCompletion,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_mini))
                    .clickable { onItemClick(item) })
        }
    }
}

@Composable
private fun InventoryItem(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    var completedTask by remember{ mutableStateOf(task.completado?:false) }

    val cardBackgroundColor by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.tertiaryContainer else
            MaterialTheme.colorScheme.primaryContainer, label = ""
    )

    val cardBackgroundColorCompleted =
        if (task.completado) MaterialTheme.colorScheme.inverseOnSurface
        else MaterialTheme.colorScheme.surfaceVariant

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
                .padding(dimensionResource(id = R.dimen.padding_small))
            /*
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .background(
                    if (task.completado) MaterialTheme.colorScheme.inverseOnSurface
                    else MaterialTheme.colorScheme.surfaceVariant
                ),

            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            */
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                titleContentTask(task, modifier = Modifier.weight(0.6f))
                //Spacer(modifier = Modifier.weight(1f))
                iconoHeroe(imaxeId = R.drawable.gym_logo_vector_617585_1980)
            }

            Row {
                contentRestantesTask(task)
            }
            Row {
                infoHeroe(title = task.name, description = task.name)

            }
            Row {
                expandButton(expanded = expanded, onClick = { expanded = !expanded })
                contentCompletadoTask(task = task, updateTaskCompletion = updateTaskCompletion)

            }
            if (expanded) {
                //extraHeroe(extra = task.quantity, modifier)
                Text(text = "prueba")
            }

        }
    }
}

@Composable
fun contentCompletadoTask(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = stringResource(R.string.completado)
        )
        Spacer(Modifier.weight(1f))
        CheckBoxCompletado(
            task = task,
            updateTaskCompletion = updateTaskCompletion
        )
    }
}

@Composable
fun contentRestantesTask(
    task: Task
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                R.string.series_restantes,
                task.quantity
            ),
            style = MaterialTheme.typography.titleMedium
        )
    }

}

@Composable
fun titleContentTask(
    task: Task,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = task.name,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}



@Composable
fun infoHeroe(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
fun extraHeroe(
    @StringRes extra: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "About:",
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Default,
            fontSize = 15.sp
        )
        Text(
            text = stringResource(id = extra)
        )


    }

}

@Composable
fun iconoHeroe(
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
fun CheckBoxCompletado(
    task: Task,
    updateTaskCompletion: (Int, Boolean) -> Unit,
) {
    var checked by remember { mutableStateOf(task.completado) }

    Switch(
        checked = checked,
        onCheckedChange = { isChecked ->
            checked = isChecked
            updateTaskCompletion(task.id,isChecked)
        },
        modifier = Modifier.padding(0.dp)
    )
}




/*
@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    InventoryTheme {
        HomeBody(listOf(
            Task(1, "Game", 1, 20), Task(2, "Pen", 2, 30), Task(3, "TV", 3, 50)
        ), onItemClick = {}, updateTaskCompletion = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    InventoryTheme {
        HomeBody(listOf(), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryItemPreview() {
    InventoryTheme {
        InventoryItem(
            Task(1, "Game", 1, 20),
        )
    }
}
*/