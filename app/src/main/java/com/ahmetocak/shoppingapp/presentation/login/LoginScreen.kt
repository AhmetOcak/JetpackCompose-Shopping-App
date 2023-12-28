package com.ahmetocak.shoppingapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import com.ahmetocak.shoppingapp.presentation.login.components.ForgotPasswordButton
import com.ahmetocak.shoppingapp.presentation.login.components.ForgotPasswordDialog
import com.ahmetocak.shoppingapp.presentation.login.components.RegisterNow
import com.ahmetocak.shoppingapp.presentation.login.components.RememberMeBox

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }

    if (uiState.isResetPasswordMailSend) {
        ShoppingShowToastMessage(
            message = stringResource(id = R.string.password_reset_mail)
        )
        viewModel.consumedResetPasswordState()
    }

    if (uiState.showPasswordResetDialog) {
        ForgotPasswordDialog(
            modifier = modifier,
            forgotPassValue = viewModel.passwordResetEmail,
            onForgotPassValChange = viewModel::updatePasswordResetEmail,
            isForgotPassFieldError = uiState.resetPasswordEmailFieldErrorMessage != null,
            forgotPassFieldLabel = if (uiState.resetPasswordEmailFieldErrorMessage != null) {
                uiState.resetPasswordEmailFieldErrorMessage ?: stringResource(
                    id = R.string.unknown_error
                )
            } else {
                stringResource(id = R.string.enter_email)
            },
            onDismissRequest = viewModel::consumedResetPasswordState,
            onSendPasswordResetEmailClick = viewModel::sendPasswordResetEmail
        )
    }

    LoginScreenContent(
        modifier = modifier,
        emailValue = viewModel.email,
        passwordValue = viewModel.password,
        onEmailValueChange = viewModel::updateEmailField,
        onPasswordValueChange = viewModel::updatePasswordField,
        emailFieldError = uiState.emailFieldErrorMessage != null,
        emailFieldLabel = if (uiState.emailFieldErrorMessage == null) {
            stringResource(id = R.string.enter_email)
        } else {
            uiState.emailFieldErrorMessage ?: stringResource(id = R.string.unknown_error)
        },
        passwordFieldError = uiState.passwordFieldErrorMessage != null,
        passwordFieldLabel = if (uiState.passwordFieldErrorMessage == null) {
            stringResource(id = R.string.enter_password)
        } else {
            uiState.passwordFieldErrorMessage ?: stringResource(id = R.string.unknown_error)
        },
        checked = viewModel.rememberMe,
        onCheckedChange = viewModel::updateRememberMeBox,
        onLoginClicked = { viewModel.login(onLoginClick) },
        onRegisterClick = onSignUpClick,
        onForgotPasswordClick = viewModel::showPasswordResetDialog,
        isLoading = uiState.isLoading,
        isLoginEnd = uiState.isLoginEnd
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier,
    emailValue: String,
    passwordValue: String,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    emailFieldLabel: String,
    passwordFieldError: Boolean,
    passwordFieldLabel: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    onLoginClicked: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoading: Boolean,
    isLoginEnd: Boolean
) {
    AuthBackground(modifier)

    if (isLoading) {
        FullScreenCircularLoading()
    } else if (!isLoginEnd) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
                .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            WelcomeText(modifier = modifier, text = stringResource(id = R.string.hello_again))
            AuthEnterEmailOtf(
                modifier = modifier,
                value = emailValue,
                onValueChange = onEmailValueChange,
                isError = emailFieldError,
                labelText = emailFieldLabel
            )
            AuthEnterPasswordOtf(
                modifier = modifier,
                value = passwordValue,
                onValueChange = onPasswordValueChange,
                isError = passwordFieldError,
                labelText = passwordFieldLabel,
                keyboardActions = KeyboardActions(
                    onDone = { onLoginClicked() }
                )
            )
            RememberMeBox(
                modifier = modifier,
                checked = checked,
                onCheckedChange = { onCheckedChange() }
            )
            ShoppingButton(
                modifier = modifier,
                onClick = onLoginClicked,
                buttonText = stringResource(id = R.string.login)
            )
            ForgotPasswordButton(onForgotPasswordClick = onForgotPasswordClick)
            RegisterNow(modifier = modifier, onRegisterClick = onRegisterClick)
        }
    }
}