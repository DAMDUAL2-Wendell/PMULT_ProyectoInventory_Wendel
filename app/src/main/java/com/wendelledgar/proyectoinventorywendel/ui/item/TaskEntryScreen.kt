package com.wendelledgar.proyectoinventorywendel.ui.item

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.topAppBar
import com.wendelledgar.proyectoinventorywendel.ui.AppViewModelProvider
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import com.wendelledgar.proyectoinventorywendel.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object ItemEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            topAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp //navigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack() //popBackStack
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
fun ItemEntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {


        outliedTextField(
            value = itemDetails.name,
            label = R.string.item_name_req,
            onValueChange = { onValueChange(itemDetails.copy(name = it)) },
            keyboardType = KeyboardType.Text,
        )

        outliedTextField(
            value = itemDetails.description,
            label = R.string.description,
            onValueChange = { onValueChange(itemDetails.copy(description = it)) },
            keyboardType = KeyboardType.Text,
        )

        outliedTextField(
            value = itemDetails.quantity,
            label = R.string.cantidad_series,
            onValueChange = { onValueChange(itemDetails.copy(quantity = it.toInt())) },
            keyboardType = KeyboardType.Decimal,
        )

        sliderAndCheckbox(
            sliderValue = itemDetails.serie1?:0,
            sliderNumero = 1,
            completed = itemDetails.completed?:false,
            totalRepeticiones = itemDetails.quantity?:15,
            updateSliderTask = {uno,dos->{}}
        )

        /*
        OutlinedTextField(
            value = itemDetails.quantity,
            onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
            label = { Text(stringResource(R.string.cantidad_series)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
*/


/*
        OutlinedTextField(
            value = itemDetails.seriesRealizadas,
            onValueChange = { onValueChange(itemDetails.copy(seriesRealizadas = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.item_seriesRealizadas_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        */
        if (enabled) {
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
private fun ItemEntryScreenPreview() {
    InventoryTheme {
        ItemEntryBody(itemUiState = ItemUiState(
            ItemDetails(
                name = "Item name", seriesRealizadas = "1", quantity = 5
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}
