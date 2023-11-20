package com.ahmetocak.shoppingapp.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.profile.InfoType

@Composable
fun UpdateAccountInfoDialog(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    updateValue: String,
    onUpdateValueChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    title: String,
    infoType: InfoType
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Update $title",
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    value = updateValue,
                    onValueChange = onUpdateValueChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (infoType) {
                            InfoType.MOBILE -> { KeyboardType.Number }
                            else -> { KeyboardType.Text }
                        }
                    )
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.two_level_margin)))
                    Button(
                        onClick = onUpdateClick,
                        enabled = updateValue.isNotBlank()
                    ) {
                        Text(text = stringResource(id = R.string.update))
                    }
                }
            }
        }
    }
}