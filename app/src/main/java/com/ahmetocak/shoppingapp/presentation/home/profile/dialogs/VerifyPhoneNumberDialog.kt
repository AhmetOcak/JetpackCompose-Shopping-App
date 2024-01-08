package com.ahmetocak.shoppingapp.presentation.home.profile.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.ComponentPreview

@Composable
fun VerifyPhoneNumberDialog(
    codeValue: String,
    onCodeValueChange: (String) -> Unit,
    verifyPhoneNumber: () -> Unit,
    onVerifyPhoneNumberDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onVerifyPhoneNumberDismiss) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = stringResource(id = R.string.enter_verification_code))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    value = codeValue,
                    onValueChange = onCodeValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Button(
                    onClick = verifyPhoneNumber,
                    enabled = codeValue.isNotBlank()
                ) {
                    Text(text = stringResource(id = R.string.verify))
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun VerifyPhoneNumberDialogPreview() {
    ShoppingAppTheme {
        Surface {
            VerifyPhoneNumberDialog(
                codeValue = "",
                onCodeValueChange = {},
                verifyPhoneNumber = {},
                onVerifyPhoneNumberDismiss = {}
            )
        }
    }
}