package com.ahmetocak.shoppingapp.presentation.home.profile.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.presentation.designsystem.components.AuthEnterPasswordOtf

@Composable
fun DeleteAccountDialog(
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit,
    emailValue: String,
    passwordValue: String,
    onEmailValChange: (String) -> Unit,
    onPasswordValChange: (String) -> Unit
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
                    text = stringResource(id = R.string.delete_account_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(id = R.string.delete_account_warning),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Red
                )
                AuthEnterEmailOtf(
                    value = emailValue,
                    onValueChange = onEmailValChange,
                    isError = false,
                    labelText = stringResource(id = R.string.enter_email)
                )
                AuthEnterPasswordOtf(
                    value = passwordValue,
                    onValueChange = onPasswordValChange,
                    isError = false,
                    labelText = stringResource(id = R.string.enter_password)
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
                            onDeleteClick()
                        },
                        enabled = emailValue.isNotBlank() && passwordValue.isNotBlank() && !isChangeRequestMade
                    ) {
                        if (isChangeRequestMade) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteAccountDialogPreview() {
    DeleteAccountDialog(
        onDismissRequest = {},
        onDeleteClick = {},
        emailValue = "",
        passwordValue = "",
        onEmailValChange = {},
        onPasswordValChange = {}
    )
}