package com.ahmetocak.shoppingapp.presentation.login

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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.components.AuthBackground
import com.ahmetocak.shoppingapp.presentation.designsystem.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.presentation.designsystem.components.AuthEnterPasswordOtf
import com.ahmetocak.shoppingapp.presentation.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingButton
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.presentation.designsystem.components.WelcomeText
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.CustomPreview

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
        Dialog(onDismissRequest = viewModel::consumedResetPasswordState) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AuthEnterEmailOtf(
                        value = viewModel.passwordResetEmail,
                        onValueChange = viewModel::updatePasswordResetEmail,
                        isError = uiState.resetPasswordEmailFieldErrorMessage != null,
                        labelText = if (uiState.resetPasswordEmailFieldErrorMessage != null) {
                            uiState.resetPasswordEmailFieldErrorMessage ?: stringResource(
                                id = R.string.unknown_error
                            )
                        } else {
                            stringResource(id = R.string.enter_email)
                        }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.two_level_margin)))
                    ShoppingButton(
                        onClick = viewModel::sendPasswordResetEmail,
                        buttonText = stringResource(id = R.string.send)
                    )
                }
            }
        }
    }

    ShoppingScaffold(modifier = modifier) { paddingValues ->
        AuthBackground()
        LoginScreenContent(
            modifier = Modifier.padding(paddingValues),
            emailValue = viewModel.email,
            passwordValue = viewModel.password,
            onEmailValueChange = remember(viewModel) { viewModel::updateEmailField },
            onPasswordValueChange = remember(viewModel) { viewModel::updatePasswordField },
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
            onCheckedChange = remember(viewModel) { viewModel::updateRememberMeBox },
            onLoginClicked = remember(viewModel) { { viewModel.login(onLoginClick) } },
            onRegisterClick = onSignUpClick,
            onForgotPasswordClick = viewModel::showPasswordResetDialog,
            isLoading = uiState.isLoading,
            isLoginEnd = uiState.isLoginEnd
        )
    }
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
    if (isLoading) {
        FullScreenCircularLoading()
    } else if (!isLoginEnd) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            WelcomeText(modifier = Modifier, text = stringResource(id = R.string.hello_again))
            AuthEnterEmailOtf(
                value = emailValue,
                onValueChange = onEmailValueChange,
                isError = emailFieldError,
                labelText = emailFieldLabel
            )
            AuthEnterPasswordOtf(
                value = passwordValue,
                onValueChange = onPasswordValueChange,
                isError = passwordFieldError,
                labelText = passwordFieldLabel
            )
            RememberMeBox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
            ShoppingButton(
                modifier = Modifier,
                onClick = onLoginClicked,
                buttonText = stringResource(id = R.string.login)
            )
            TextButton(onClick = onForgotPasswordClick) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.no_account))
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = onRegisterClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.register_now),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RememberMeBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
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
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.remember_me))
    }
}

@CustomPreview
@Composable
private fun LoginScreenPreview() {
    ShoppingAppTheme {
        Surface {
            AuthBackground()
            LoginScreenContent(
                modifier = Modifier,
                emailValue = "",
                passwordValue = "",
                onEmailValueChange = {},
                onPasswordValueChange = {},
                emailFieldError = false,
                emailFieldLabel = "Email",
                passwordFieldError = false,
                passwordFieldLabel = "Password",
                checked = false,
                onCheckedChange = {},
                onLoginClicked = {},
                onRegisterClick = {},
                onForgotPasswordClick = {},
                isLoading = false,
                isLoginEnd = false
            )
        }
    }
}