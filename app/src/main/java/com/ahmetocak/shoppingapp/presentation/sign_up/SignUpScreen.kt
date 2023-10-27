package com.ahmetocak.shoppingapp.presentation.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.ui.components.AuthBackground
import com.ahmetocak.shoppingapp.ui.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.ui.components.AuthEnterPasswordOtf
import com.ahmetocak.shoppingapp.ui.components.ShoppingButton
import com.ahmetocak.shoppingapp.ui.components.WelcomeText

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {

    SignUpScreenContent(modifier = modifier)
}

@Composable
private fun SignUpScreenContent(modifier: Modifier) {
    AuthBackground(modifier = modifier)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
            .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        WelcomeText(modifier = modifier, text = stringResource(id = R.string.welcome_register))
        AuthEnterEmailOtf(modifier = modifier, value = "", onValueChange = {})
        AuthEnterPasswordOtf(modifier = modifier, value = "", onValueChange = {})
        AuthEnterPasswordOtf(
            modifier = modifier,
            value = "",
            onValueChange = {},
            isVerifyVersion = true
        )
        ShoppingButton(
            modifier = modifier.padding(top = dimensionResource(id = R.dimen.one_level_margin)),
            onClick = {},
            buttonText = stringResource(id = R.string.sign_up)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSignUpScreen() {
    SignUpScreen()
}