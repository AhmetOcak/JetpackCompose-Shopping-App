package com.ahmetocak.shoppingapp.presentation.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.AuthFieldCheckers
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.domain.repository.FirebaseRepository
import com.ahmetocak.shoppingapp.model.auth.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var verifyPassword by mutableStateOf("")
        private set

    fun updateEmailField(value: String) {
        email = value
    }

    fun updatePasswordField(value: String) {
        password = value
    }

    fun updateVerifyPasswordField(value: String) {
        verifyPassword = value
    }

    fun signUp(onNavigate: () -> Unit) {
        val isEmailOk = AuthFieldCheckers.checkEmailField(
            email = email,
            onBlank = {
                _uiState.update {
                    it.copy(
                        emailFieldErrorMessage = UiText.StringResource(R.string.fill_this_field)
                    )
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(
                        emailFieldErrorMessage = UiText.StringResource(R.string.enter_valid_email)
                    )
                }
            },
            onCheckSuccess = {
                _uiState.update { it.copy(emailFieldErrorMessage = null) }
            }
        )

        val isPasswordOk = AuthFieldCheckers.checkPassword(
            password = password,
            onBlank = {
                _uiState.update {
                    it.copy(
                        passwordFieldErrorMessage = UiText.StringResource(R.string.fill_this_field)
                    )
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = UiText.StringResource(R.string.pass_length))
                }
            },
            onCheckSuccess = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = null)
                }
            }
        )

        val isVerifyPasswordOk = checkVerifyPasswordField()

        if (isEmailOk && isPasswordOk && isVerifyPasswordOk) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                firebaseRepository.createAccount(auth = Auth(email, password))
                    .addOnCompleteListener { signUpTask ->
                        if (signUpTask.isSuccessful) {
                            firebaseRepository.sendEmailVerification()
                                ?.addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        _uiState.update {
                                            it.copy(isLoading = false, isSignUpEnd = true)
                                        }
                                        onNavigate()
                                    } else {
                                        _uiState.update {
                                            it.copy(
                                                isLoading = false,
                                                errorMessages = listOf(
                                                    signUpTask.exception?.message?.let { message ->
                                                        UiText.DynamicString(message)
                                                    } ?: kotlin.run {
                                                        UiText.StringResource(R.string.unknown_error)
                                                    }
                                                )
                                            )
                                        }
                                    }
                                }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessages = listOf(
                                        signUpTask.exception?.message?.let { message ->
                                            UiText.DynamicString(message)
                                        } ?: kotlin.run {
                                            UiText.StringResource(R.string.unknown_error)
                                        }
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun checkVerifyPasswordField(): Boolean {
        return if (verifyPassword.isBlank()) {
            _uiState.update {
                it.copy(verifyPasFieldErrorMessage = UiText.StringResource(R.string.fill_this_field))
            }
            false
        } else if (verifyPassword != password) {
            _uiState.update {
                it.copy(verifyPasFieldErrorMessage = UiText.StringResource(R.string.pass_dont_match))
            }
            false
        } else {
            _uiState.update {
                it.copy(verifyPasFieldErrorMessage = null)
            }
            true
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class SignUpUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<UiText> = listOf(),
    val isSignUpEnd: Boolean = false,
    val emailFieldErrorMessage: UiText? = null,
    val passwordFieldErrorMessage: UiText? = null,
    val verifyPasFieldErrorMessage: UiText? = null
)