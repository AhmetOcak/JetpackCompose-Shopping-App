package com.ahmetocak.shoppingapp.presentation.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.helpers.AuthFieldCheckers
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
                _uiState.update { it.copy(emailFieldErrorMessage = "Please fill in this field") }
            },
            onUnValid = {
                _uiState.update { it.copy(emailFieldErrorMessage = "Please enter a valid email") }
            },
            onCheckSuccess = {
                _uiState.update { it.copy(emailFieldErrorMessage = null) }
            }
        )

        val isPasswordOk = AuthFieldCheckers.checkPassword(
            password = password,
            onBlank = {
                _uiState.update { it.copy(passwordFieldErrorMessage = "Please fill in this field") }
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

        val isVerifyPasswordOk = checkVerifyPasswordField()

        if (isEmailOk && isPasswordOk && isVerifyPasswordOk) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                firebaseRepository.createAccount(auth = Auth(email, password))
                    .addOnCompleteListener { signUpTask ->
                        if (signUpTask.isSuccessful) {
                            firebaseRepository.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    _uiState.update {
                                        it.copy(isLoading = false, isSignUpEnd = true)
                                    }
                                    onNavigate()
                                } else {
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            errorMessage = signUpTask.exception?.message
                                        )
                                    }
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = signUpTask.exception?.message
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
                it.copy(verifyPasFieldErrorMessage = "Please fill in this field")
            }
            false
        } else if (verifyPassword != password) {
            _uiState.update {
                it.copy(verifyPasFieldErrorMessage = "Passwords do not match")
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
            it.copy(errorMessage = null)
        }
    }
}

data class SignUpUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpEnd: Boolean = false,
    val emailFieldErrorMessage: String? = null,
    val passwordFieldErrorMessage: String? = null,
    val verifyPasFieldErrorMessage: String? = null
)