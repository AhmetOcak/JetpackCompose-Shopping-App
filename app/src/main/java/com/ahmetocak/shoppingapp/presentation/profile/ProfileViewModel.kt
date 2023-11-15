package com.ahmetocak.shoppingapp.presentation.profile

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.shoppingapp.model.user_detail.UserDetail
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val UNKNOWN_ERROR = "Something went wrong. Please try again later."

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirebaseRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getUserProfileImage()
        getUserDetails()

        val user = auth.currentUser

        if (user == null) {
            _uiState.update {
                it.copy(errorMessages = listOf(UNKNOWN_ERROR))
            }
        } else {
            _uiState.update {
                it.copy(
                    name = user.displayName,
                    email = user.email,
                    phoneNumber = user.phoneNumber
                )
            }
        }
    }

    var updateValue by mutableStateOf("")
        private set

    var verificationCode by mutableStateOf("")
        private set

    var infoType by mutableStateOf(InfoType.NAME)
        private set

    private var storedVerificationId: String? = null

    fun updateAccountInfoValue(value: String) {
        updateValue = value
    }

    fun updateVerificationCodeValue(value: String) {
        verificationCode = value
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

            InfoType.ADDRESS -> {
                _uiState.value.userDetail?.address ?: ""
            }

            else -> { "" }
        }
    }

    fun updateUserName() {
        if (updateValue.isNotBlank()) {
            viewModelScope.launch(ioDispatcher) {
                repository.updateUserProfile(userProfileChangeRequest {
                    displayName = updateValue
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(
                                name = auth.currentUser?.displayName,
                                userMessages = listOf("Name updated successfully")
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR))
                        }
                    }
                }
            }
        }
    }

    fun updateUserPhoto(uri: Uri) {
        viewModelScope.launch(ioDispatcher) {
            repository.uploadUserProfileImage(uri).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(userMessages = listOf("Image uploaded successfully"))
                    }
                    getUserProfileImage()
                } else {
                    _uiState.update {
                        it.copy(errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR))
                    }
                }
            }
        }
    }

    private fun getUserProfileImage() {
        viewModelScope.launch(ioDispatcher) {
            repository.getUserProfileImage().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(photoUrl = task.result)
                    }
                } else {
                    _uiState.update {
                        it.copy(errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR))
                    }
                }
            }
        }
    }

    fun sendVerificationCode(activity: Activity) {
        viewModelScope.launch(ioDispatcher) {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                override fun onVerificationFailed(e: FirebaseException) {
                    _uiState.update {
                        it.copy(
                            errorMessages = listOf(e.message ?: UNKNOWN_ERROR),
                            verifyPhoneNumber = VerifyPhoneNumberState.NOTHING
                        )
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    storedVerificationId = verificationId
                    _uiState.update {
                        it.copy(verifyPhoneNumber = VerifyPhoneNumberState.ON_CODE_SENT)
                    }
                }
            }

            repository.sendVerificationCode(updateValue, activity, callbacks)
        }
    }

    fun verifyUserPhoneNumber() {
        repository.verifyUserPhoneNumber(
            PhoneAuthProvider.getCredential(
                storedVerificationId ?: "",
                verificationCode
            )
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.update {
                    it.copy(
                        verifyPhoneNumber = VerifyPhoneNumberState.ON_VERIFICATION_COMPLETED,
                        phoneNumber = auth.currentUser?.phoneNumber
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        verifyPhoneNumber = VerifyPhoneNumberState.NOTHING,
                        errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR)
                    )
                }
            }
        }
    }

    fun uploadUserAddress() {
        viewModelScope.launch(ioDispatcher) {
            repository.uploadUserAddress(updateValue, auth.uid ?: "null")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(userMessages = listOf("Your address updated successfully"))
                        }
                        getUserDetails()
                    } else {
                        _uiState.update {
                            it.copy(errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR))
                        }
                    }
                }
        }
    }

    fun updateUserBirthdate(birthdate: Long) {
        viewModelScope.launch(ioDispatcher) {
            repository.uploadUserBirthdate(birthdate, auth.uid ?: "").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(userMessages = listOf("Your birthdate updated successfully"))
                    }
                } else {
                    _uiState.update {
                        it.copy(errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR))
                    }
                }
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch(ioDispatcher) {
            repository.getAllUserDetails(auth.uid ?: "").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(userDetail = task.result.toObject<UserDetail>())
                    }
                } else {
                    _uiState.update {
                        it.copy(errorMessages = listOf(task.exception?.message ?: UNKNOWN_ERROR))
                    }
                }
            }
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = listOf())
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<String> = listOf(),
    val name: String? = null,
    val email: String? = null,
    val photoUrl: Uri? = null,
    val phoneNumber: String? = null,
    val userMessages: List<String> = listOf(),
    val verifyPhoneNumber: VerifyPhoneNumberState = VerifyPhoneNumberState.NOTHING,
    val userDetail: UserDetail? = null
)

enum class VerifyPhoneNumberState {
    NOTHING,
    ON_CODE_SENT,
    ON_VERIFICATION_COMPLETED
}