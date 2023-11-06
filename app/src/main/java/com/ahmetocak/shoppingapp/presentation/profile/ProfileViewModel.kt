package com.ahmetocak.shoppingapp.presentation.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val user = auth.currentUser

        if (user == null) {
            _uiState.update {
                it.copy(isError = true)
            }
        } else {
            _uiState.update {
                it.copy(
                    name = user.displayName,
                    email = user.email,
                    photoUrl = user.photoUrl,
                    phoneNumber = user.phoneNumber
                )
            }
        }
    }

    var updateValue by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var infoType by mutableStateOf(InfoType.NAME)
        private set

    fun updateAccountInfoValue(value: String) {
        updateValue = value
    }

    fun clearAccountInfoValue() {
        updateValue = ""
    }

    fun setAccountInfoType(infoType: InfoType) {
        this.infoType = infoType

        updateValue = when (infoType) {
            InfoType.NAME -> {
                _uiState.value.name ?: ""
            }

            InfoType.MOBILE -> {
                _uiState.value.phoneNumber ?: ""
            }

            InfoType.EMAIL -> {
                _uiState.value.email ?: ""
            }

            InfoType.ADDRESS -> {
                _uiState.value.address ?: ""
            }

            InfoType.DOB -> {
                _uiState.value.dob ?: ""
            }
        }
    }

    fun updateUserName() {
        if (updateValue.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.update { it.copy(isLoading = true) }
                repository.updateUserProfile(userProfileChangeRequest {
                    displayName = updateValue
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                name = auth.currentUser?.displayName
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessages = listOf(task.exception?.message ?: "null")
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateUserPhoto(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            repository.updateUserProfile(userProfileChangeRequest {
                photoUri = uri
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            photoUrl = auth.currentUser?.photoUrl
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessages = listOf(task.exception?.message ?: "null")
                        )
                    }
                }
            }
        }
    }

/*    fun reauthenticateAndUpdateMail() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            repository.reAuthenticate(
                auth = Auth(auth.currentUser?.email ?: "", "ahmet2000")
            )?.addOnCompleteListener {  task ->
                if (task.isSuccessful) {
                    updateUserEmail()
                } else {
                    Log.d("Reauthenticate", task.exception?.message.toString())
                }
            }
        }
    }

    private fun updateUserEmail() {
        auth.currentUser?.updateEmail(updateValue)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        email = auth.currentUser?.email
                    )
                }
            } else {
                Log.d("UpdateUserEmail", task.exception?.message.toString())
            }
        }
    }*/
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessages: List<String> = listOf(),
    val name: String? = null,
    val email: String? = null,
    val photoUrl: Uri? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val dob: String? = null
)