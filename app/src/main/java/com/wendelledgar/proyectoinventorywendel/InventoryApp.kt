package com.wendelledgar.proyectoinventorywendel

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wendelledgar.proyectoinventorywendel.ui.navigation.InventoryNavHost

@Composable
fun InventoryApp(
    navController:  NavHostController = rememberNavController()
) {
    InventoryNavHost(
        navController = navController
    )
}