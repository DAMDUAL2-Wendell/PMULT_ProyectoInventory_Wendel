@file:OptIn(ExperimentalMaterial3Api::class)

package com.wendelledgar.proyectoinventorywendel

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wendelledgar.proyectoinventorywendel.ui.navigation.TaskNavHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskApp(navController: NavHostController = rememberNavController()) {
    TaskNavHost(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        /*
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        */

        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    //.background(color = MaterialTheme.colorScheme.inversePrimary)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.running_icon_svg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_topAppbar_size))
                        .padding(dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall
                )
            }

        }
    )
}

@Composable
fun bottomAppBarHome(
    onClickAddTask : () -> Unit
) {
    androidx.compose.material3.BottomAppBar(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.bottom_bar)),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        actions = {
            IconButton(onClick = {}) {
                Icon(Filled.Home, contentDescription = "")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickAddTask,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),

            ) {
                Icon(Filled.Add, "")
            }
        }
    )

}

@Composable
fun bottomAppBarDetail(
    navigateBack : () -> Unit
) {
    androidx.compose.material3.BottomAppBar(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.bottom_bar)),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        actions = {
            IconButton(onClick = navigateBack) {
                Icon(Filled.Home, contentDescription = "")
            }
        }
    )

}


