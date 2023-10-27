package com.ahmetocak.shoppingapp.presentation.sign_up

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.ui.components.AuthBackground
import com.ahmetocak.shoppingapp.ui.components.AuthEnterEmailOtf
import com.ahmetocak.shoppingapp.ui.components.AuthEnterPasswordOtf
import com.ahmetocak.shoppingapp.ui.components.ShoppingButton
import com.ahmetocak.shoppingapp.ui.components.WelcomeText

@Composable
fun SignUpScreen(modifier: Modifier = Modifier, onNavigate: () -> Unit) {

    val viewModel: SignUpViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessage != null) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessage,
            Toast.LENGTH_LONG
        ).show()
    }

    SignUpScreenContent(
        modifier = modifier,
        emailValue = viewModel.email,
        onEmailValueChange = { viewModel.updateEmailField(it) },
        emailFieldError = uiState.emailFieldErrorMessage != null,
        passwordValue = viewModel.password,
        onPasswordChange = { viewModel.updatePasswordField(it) },
        passwordFieldError = uiState.passwordFieldErrorMessage != null,
        verifyPasswordValue = viewModel.verifyPassword,
        onVerifyPasswordChange = { viewModel.updateVerifyPasswordField(it) },
        verifyPasswordFieldError = uiState.verifyPasFieldErrorMessage != null,
        emailLabel = if (uiState.emailFieldErrorMessage == null) {
            stringResource(id = R.string.enter_email)
        } else {
            uiState.emailFieldErrorMessage ?: stringResource(id = R.string.unknown_error)
        },
        passwordLabel = if (uiState.passwordFieldErrorMessage == null) {
            stringResource(id = R.string.enter_password)
        } else {
            uiState.passwordFieldErrorMessage ?: stringResource(id = R.string.unknown_error)
        },
        verifyPasswordLabel = if (uiState.verifyPasFieldErrorMessage == null) {
            stringResource(id = R.string.verify_password)
        } else {
            uiState.verifyPasFieldErrorMessage ?: stringResource(id = R.string.verify_password)
        },
        onSignUpClick = { viewModel.signUp(onNavigate) },
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
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
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
                isVerifyVersion = true,
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