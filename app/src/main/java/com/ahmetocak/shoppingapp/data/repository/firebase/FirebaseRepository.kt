package com.ahmetocak.shoppingapp.data.repository.firebase

import android.net.Uri
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.UserProfileChangeRequest
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
}