package com.ahmetocak.shoppingapp.domain.repository

import android.app.Activity
import android.net.Uri
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask

interface FirebaseRepository {

    fun createAccount(auth: Auth) : Task<AuthResult>

    fun login(auth: Auth) : Task<AuthResult>

    fun updateUserProfile(profileUpdates: UserProfileChangeRequest) : Task<Void>?

    fun sendEmailVerification() : Task<Void>?

    fun changePassword(newPassword: String) : Task<Void>?

    fun sendResetPasswordEmail(emailAddress: String) : Task<Void>?

    fun deleteAccount() : Task<Void>?

    fun reAuthenticate(auth: Auth) : Task<Void>?

    fun uploadUserProfileImage(imgUri: Uri) : UploadTask

    fun getUserProfileImage() : Task<Uri>

    fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    fun verifyUserPhoneNumber(phoneAuthCredential: PhoneAuthCredential): Task<Void>?

    fun uploadUserAddress(address: String): Task<Void>

    fun uploadUserBirthdate(birthdate: Long): Task<Void>

    fun getAllUserDetails(): Task<DocumentSnapshot>

    fun uploadUserFCMToken(token: String): Task<Void>

    fun getFCMToken(): Task<String>

    fun deleteUserFCMToken(): Task<Void>

    fun deleteUserProfileImage(): Task<Void>

    fun deleteUserData(): Task<Void>
}