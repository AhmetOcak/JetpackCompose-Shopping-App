package com.ahmetocak.shoppingapp.presentation.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.AuthBackground
import com.ahmetocak.shoppingapp.designsystem.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.designsystem.components.AuthEnterPasswordOtf
import com.ahmetocak.shoppingapp.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingButton
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.designsystem.components.WelcomeText

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }

    SignUpScreenContent(
        modifier = modifier,
        emailValue = viewModel.email,
        onEmailValueChange = viewModel::updateEmailField,
        emailFieldError = uiState.emailFieldErrorMessage != null,
        passwordValue = viewModel.password,
        onPasswordChange = viewModel::updatePasswordField,
        passwordFieldError = uiState.passwordFieldErrorMessage != null,
        verifyPasswordValue = viewModel.verifyPassword,
        onVerifyPasswordChange = viewModel::updateVerifyPasswordField,
        verifyPasswordFieldError = uiState.verifyPasFieldErrorMessage != null,
        emailLabel = uiState.emailFieldErrorMessage?.asString()
            ?: stringResource(id = R.string.enter_email),
        passwordLabel = uiState.passwordFieldErrorMessage?.asString()
            ?: stringResource(id = R.string.enter_password),
        verifyPasswordLabel = uiState.verifyPasFieldErrorMessage?.asString()
            ?: stringResource(id = R.string.verify_password),
        onSignUpClick = { viewModel.signUp(upPress) },
        isLoading = uiState.isLoading,
        isSignUpEnd = uiState.isSignUpEnd
    )
}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    passwordFieldError: Boolean,
    verifyPasswordValue: String,
    onVerifyPasswordChange: (String) -> Unit,
    verifyPasswordFieldError: Boolean,
    emailLabel: String,
    passwordLabel: String,
    verifyPasswordLabel: String,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    isSignUpEnd: Boolean
) {
    AuthBackground(modifier = modifier)

    if (isLoading) {
        FullScreenCircularLoading()
    } else if (!isSignUpEnd) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
                .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            WelcomeText(modifier = modifier, text = stringResource(id = R.string.welcome_register))
            AuthEnterEmailOtf(
                modifier = modifier,
                value = emailValue,
                onValueChange = onEmailValueChange,
                labelText = emailLabel,
                isError = emailFieldError
            )
            AuthEnterPasswordOtf(
                modifier = modifier,
                value = passwordValue,
                onValueChange = onPasswordChange,
                labelText = passwordLabel,
                isError = passwordFieldError
            )
            AuthEnterPasswordOtf(
                modifier = modifier,
                value = verifyPasswordValue,
                onValueChange = onVerifyPasswordChange,
                labelText = verifyPasswordLabel,
                isError = verifyPasswordFieldError
            )
            ShoppingButton(
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                onClick = onSignUpClick,
                buttonText = stringResource(id = R.string.sign_up)
            )
        }
    }
}