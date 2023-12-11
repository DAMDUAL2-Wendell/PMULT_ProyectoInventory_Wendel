package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.inventory.R
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination

object ItemEntryDestination: NavigationDestination{
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
        
}

@Composable
fun ItemEntryScreen(
    
) {

    Text(text = "Item Entry Screen")
    
}