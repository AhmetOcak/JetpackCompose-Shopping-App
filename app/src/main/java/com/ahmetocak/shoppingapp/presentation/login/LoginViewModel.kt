package com.ahmetocak.shoppingapp.presentation.login

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.helpers.AuthFieldCheckers
import com.ahmetocak.shoppingapp.common.helpers.rememberMe
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepository
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
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
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

    fun updateRememberMeBox() {
        rememberMe = !rememberMe
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

                firebaseRepository.login(auth = Auth(email, password)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(isLoading = false, isLoginEnd = true)
                        }
                        if (rememberMe) {
                            sharedPreferences.edit().rememberMe()
                        }
                        onNavigate()
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = task.exception?.message)
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

                firebaseRepository.sendResetPasswordEmail(passwordResetEmail)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(isLoading = false, isResetPasswordMailSend = true)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isResetPasswordMailSend = false,
                                errorMessage = task.exception?.message
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
            it.copy(errorMessage = null)
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginEnd: Boolean = false,
    val isResetPasswordMailSend: Boolean = false,
    val showPasswordResetDialog: Boolean = false,
    val errorMessage: String? = null,
    val emailFieldErrorMessage: String? = null,
    val passwordFieldErrorMessage: String? = null,
    val resetPasswordEmailFieldErrorMessage: String? = null
)