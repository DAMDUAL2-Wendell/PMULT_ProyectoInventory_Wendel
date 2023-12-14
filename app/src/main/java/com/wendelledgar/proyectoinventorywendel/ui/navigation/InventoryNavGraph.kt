package com.wendelledgar.proyectoinventorywendel.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeDestination
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskDetailsDestination
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskDetailsScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEditDestination
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEditScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEntryDestination
import com.wendelledgar.proyectoinventorywendel.ui.item.TaskEntryScreen

@Composable
fun TaskNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToTaskEntry = { navController.navigate(TaskEntryDestination.route) },
                navigateToTaskUpdate = { navController.navigate("${TaskDetailsDestination.route}/${it}") }
            )
        }
        composable(route = TaskEntryDestination.route) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(
            route = TaskDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskDetailsDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskDetailsScreen(
                navigateToEditTask = { navController.navigate("${TaskEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() },
                navigateToHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateHome = { navController.navigate(HomeDestination.route) }
            )
        }
    }
}
