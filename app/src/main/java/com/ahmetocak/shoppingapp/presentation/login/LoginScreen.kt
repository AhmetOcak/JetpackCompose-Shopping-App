package com.ahmetocak.shoppingapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.ui.components.AuthBackground
import com.ahmetocak.shoppingapp.ui.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.ui.components.AuthEnterPasswordOtf
import com.ahmetocak.shoppingapp.ui.components.WelcomeText

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    LoginScreenContent(modifier = modifier)
}

@Composable
private fun LoginScreenContent(modifier: Modifier) {
    AuthBackground(modifier)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
            .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        WelcomeText(modifier = modifier, text = stringResource(id = R.string.hello_again))
        AuthEnterEmailOtf(modifier = modifier, value = "", onValueChange = {})
        AuthEnterPasswordOtf(modifier = modifier, value = "", onValueChange = {})
        RememberMeBox(modifier = modifier)
        LoginButton(modifier = modifier)
        ForgotPasswordButton()
        RegisterNowSection(modifier = modifier)
    }
}

@Composable
fun RegisterNowSection(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.no_account))
        Spacer(modifier = modifier.width(4.dp))
        TextButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = stringResource(id = R.string.register_now), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ForgotPasswordButton() {
    TextButton(onClick = { /*TODO*/ }) {
        Text(
            text = stringResource(id = R.string.forgot_password),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginButton(modifier: Modifier) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(text = stringResource(id = R.string.login))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberMeBox(modifier: Modifier) {
    var checked by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = !checked }
            )
        }
        Spacer(modifier = modifier.width(4.dp))
        Text(text = stringResource(id = R.string.remember_me))
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLoginScreen() {
    LoginScreen()
}