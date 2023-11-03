package com.ahmetocak.shoppingapp.presentation.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    auth: FirebaseAuth
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

        updateValue = when(infoType) {
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
}

data class ProfileUiState(
    val name: String? = null,
    val email: String? = null,
    val photoUrl: Uri? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val dob: String? = null,
    val isError: Boolean = false
)
