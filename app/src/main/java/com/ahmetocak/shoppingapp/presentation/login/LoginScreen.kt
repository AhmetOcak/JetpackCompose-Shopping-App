package com.ahmetocak.shoppingapp.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    LoginScreenContent(modifier = modifier)
}

@Composable
private fun LoginScreenContent(modifier: Modifier) {
    BackgroundImage(modifier)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
            .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        WelcomeText(modifier = modifier)
        EnterEmailSection(modifier = modifier)
        EnterPasswordSection(modifier = modifier)
        RememberMeBox(modifier = modifier)
        LoginButton(modifier = modifier)
        ForgotPasswordButton()
        RegisterNowSection(modifier = modifier)
    }
}

@Composable
private fun BackgroundImage(modifier: Modifier) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.White),
        startY = sizeImage.height.toFloat() / 1.5f,
        endY = sizeImage.height.toFloat()
    )

    Box {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp / 2)
                .onGloballyPositioned { sizeImage = it.size },
            painter = painterResource(id = R.drawable.auth_background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EnterEmailSection(modifier: Modifier) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        value = "",
        onValueChange = {},
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = null)
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EnterPasswordSection(modifier: Modifier) {
    var visibility by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        value = "",
        onValueChange = {},
        label = {
            Text(text = stringResource(id = R.string.enter_password))
        },
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                Icon(
                    painter = painterResource(
                        id = if (visibility) {
                            R.drawable.ic_visibility
                        } else {
                            R.drawable.ic_visibility_off
                        }
                    ),
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (visibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true
    )
}

@Composable
private fun WelcomeText(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        text = "Hello again !",
        style = MaterialTheme.typography.headlineLarge
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLoginScreen() {
    LoginScreen()
}