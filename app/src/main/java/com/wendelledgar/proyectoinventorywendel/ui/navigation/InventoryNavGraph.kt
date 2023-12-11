package com.wendelledgar.proyectoinventorywendel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeDestination
import com.wendelledgar.proyectoinventorywendel.ui.home.HomeScreen
import com.wendelledgar.proyectoinventorywendel.ui.item.ItemEntryDestination

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route){
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate(ItemEntryDestination.route)
                }
            )
        }
    }

}