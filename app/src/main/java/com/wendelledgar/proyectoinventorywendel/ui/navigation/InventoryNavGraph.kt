package com.wendelledgar.proyectoinventorywendel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeDestination
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemDetailsDestination
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemDetailsScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemEditDestination
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemEditScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemEntryDestination
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemEntryScreen
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
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {navController.navigate("${ItemDetailsDestination.route}/${it}")}
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
