package com.ahmetocak.shoppingapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.ui.components.AuthBackground
import com.ahmetocak.shoppingapp.ui.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.ui.components.AuthEnterPasswordOtf
import com.ahmetocak.shoppingapp.ui.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.ui.components.ShoppingButton
import com.ahmetocak.shoppingapp.ui.components.WelcomeText

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateSignUp: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessage()
    }

    if (uiState.isResetPasswordMailSend) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = R.string.password_reset_mail),
            Toast.LENGTH_LONG
        ).show()
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
        onCheckedChange = { viewModel.updateRememberMeBox() },
        onLoginClicked = { viewModel.login(onNavigateHome) },
        onRegisterClick = onNavigateSignUp,
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
    onCheckedChange: (Boolean) -> Unit,
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
                onCheckedChange = onCheckedChange
            )
            ShoppingButton(
                modifier = modifier,
                onClick = onLoginClicked,
                buttonText = stringResource(id = R.string.login)
            )
            ForgotPasswordButton(onForgotPasswordClick = onForgotPasswordClick)
            RegisterNowSection(modifier = modifier, onRegisterClick = onRegisterClick)
        }
    }
}

@Composable
private fun RegisterNowSection(modifier: Modifier, onRegisterClick: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.no_account))
        Spacer(modifier = modifier.width(4.dp))
        TextButton(
            modifier = modifier,
            onClick = onRegisterClick,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = stringResource(id = R.string.register_now), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ForgotPasswordButton(onForgotPasswordClick: () -> Unit) {
    TextButton(onClick = onForgotPasswordClick) {
        Text(
            text = stringResource(id = R.string.forgot_password),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RememberMeBox(
    modifier: Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
        Spacer(modifier = modifier.width(4.dp))
        Text(text = stringResource(id = R.string.remember_me))
    }
}

@Composable
private fun ForgotPasswordDialog(
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