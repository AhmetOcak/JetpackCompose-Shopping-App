package com.ahmetocak.shoppingapp.presentation.login

import android.annotation.SuppressLint
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.AuthFieldCheckers
import com.ahmetocak.shoppingapp.common.helpers.PreferenceManager
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.domain.repository.FirebaseRepository
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.ahmetocak.shoppingapp.utils.REMEMBER_ME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var rememberMe by mutableStateOf(false)
        private set

    var passwordResetEmail by mutableStateOf("")
        private set

    fun updateEmailField(value: String) {
        email = value
    }

    fun updatePasswordField(value: String) {
        password = value
    }

    fun updateRememberMeBox(value: Boolean) {
        rememberMe = value
    }

    fun updatePasswordResetEmail(value: String) {
        passwordResetEmail = value
    }

    @SuppressLint("CommitPrefEdits")
    fun login(onNavigate: () -> Unit) {
        val isEmailOk = AuthFieldCheckers.checkEmailField(
            email = email,
            onBlank = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = "Please fill in this field")
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = "Please enter a valid email")
                }
            },
            onCheckSuccess = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = null)
                }
            }
        )

        val isPasswordOk = AuthFieldCheckers.checkPassword(
            password = password,
            onBlank = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = "Please fill in this field")
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = "Password must be at least 6 characters in length")
                }
            },
            onCheckSuccess = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = null)
                }
            }
        )

        if (isEmailOk && isPasswordOk) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                firebaseRepository.login(auth = Auth(email, password))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _uiState.update {
                                it.copy(isLoading = false, isLoginEnd = true)
                            }
                            if (rememberMe) {
                                preferenceManager.saveData(
                                    REMEMBER_ME,
                                    true
                                )
                            }
                            onNavigate()
                        } else {
                            _uiState.update {
                                it.copy(isLoading = false, errorMessages = listOf(
                                    task.exception?.message?.let { message ->
                                        UiText.DynamicString(message)
                                    } ?: kotlin.run {
                                        UiText.StringResource(resId = R.string.unknown_error)
                                    }
                                ))
                            }
                        }
                    }
            }
        }
    }

    fun sendPasswordResetEmail() {
        val isPasswordResetEmailOk = AuthFieldCheckers.checkEmailField(
            email = passwordResetEmail,
            onBlank = {
                _uiState.update {
                    it.copy(resetPasswordEmailFieldErrorMessage = "Please fill in this field")
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(resetPasswordEmailFieldErrorMessage = "Please enter a valid email")
                }
            },
            onCheckSuccess = {
                _uiState.update {
                    it.copy(resetPasswordEmailFieldErrorMessage = null)
                }
            }
        )

        if (isPasswordResetEmailOk) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                firebaseRepository.sendResetPasswordEmail(passwordResetEmail)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _uiState.update {
                                it.copy(isLoading = false, isResetPasswordMailSend = true)
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isResetPasswordMailSend = false,
                                    errorMessages = listOf(
                                        task.exception?.message?.let { message ->
                                            UiText.DynamicString(message)
                                        } ?: kotlin.run {
                                            UiText.StringResource(resId = R.string.unknown_error)
                                        }
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    fun showPasswordResetDialog() {
        _uiState.update {
            it.copy(showPasswordResetDialog = true)
        }
    }

    fun consumedResetPasswordState() {
        passwordResetEmail = ""
        _uiState.update {
            it.copy(showPasswordResetDialog = false, isResetPasswordMailSend = false)
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginEnd: Boolean = false,
    val isResetPasswordMailSend: Boolean = false,
    val showPasswordResetDialog: Boolean = false,
    val errorMessages: List<UiText> = listOf(),
    val emailFieldErrorMessage: String? = null,
    val passwordFieldErrorMessage: String? = null,
    val resetPasswordEmailFieldErrorMessage: String? = null
)