package com.ahmetocak.shoppingapp.presentation.home.profile.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.presentation.home.profile.InfoType
import com.ahmetocak.shoppingapp.utils.ComponentPreview

@Composable
fun UpdateAccountInfoDialog(
    onDismissRequest: () -> Unit,
    updateValue: String,
    onUpdateValueChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    title: String,
    infoType: InfoType
) {
    var isChangeRequestMade by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Update $title",
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    value = updateValue,
                    onValueChange = onUpdateValueChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (infoType) {
                            InfoType.MOBILE -> {
                                KeyboardType.Phone
                            }

                            else -> {
                                KeyboardType.Text
                            }
                        }
                    ),
                    placeholder = {
                        if (infoType == InfoType.MOBILE) {
                            Text(text = "e.g. +909999999999")
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.two_level_margin)))
                    Button(
                        onClick = {
                            isChangeRequestMade = true
                            onUpdateClick()
                        },
                        enabled = if (infoType == InfoType.MOBILE) {
                            updateValue.isNotBlank() && !isChangeRequestMade && updateValue.matches(
                                Regex("""^\+\d{12}$""")
                            )
                        } else {
                            updateValue.isNotBlank() && !isChangeRequestMade
                        }
                    ) {
                        if (isChangeRequestMade) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(text = stringResource(id = R.string.update))
                        }
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun UpdateAccountInfoDialogPreview() {
    ShoppingAppTheme {
        Surface {
            UpdateAccountInfoDialog(
                onDismissRequest = {},
                updateValue = "",
                onUpdateValueChange = {},
                onUpdateClick = {},
                title = "Name",
                infoType = InfoType.NAME
            )
        }
    }
}