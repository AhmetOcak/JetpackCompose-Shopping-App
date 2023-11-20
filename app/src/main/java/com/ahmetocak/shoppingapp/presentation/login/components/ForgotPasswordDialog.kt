package com.ahmetocak.shoppingapp.presentation.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingButton

@Composable
fun ForgotPasswordDialog(
    modifier: Modifier,
    forgotPassValue: String,
    onForgotPassValChange: (String) -> Unit,
    isForgotPassFieldError: Boolean,
    forgotPassFieldLabel: String,
    onDismissRequest: () -> Unit,
    onSendPasswordResetEmailClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AuthEnterEmailOtf(
                    modifier = modifier.fillMaxWidth(),
                    value = forgotPassValue,
                    onValueChange = onForgotPassValChange,
                    isError = isForgotPassFieldError,
                    labelText = forgotPassFieldLabel
                )
                Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.two_level_margin)))
                ShoppingButton(
                    onClick = onSendPasswordResetEmailClick,
                    buttonText = stringResource(id = R.string.send)
                )
            }
        }
    }
}