package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.auth

import android.app.Activity
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

interface FirebaseAuthDataSource {

    fun createAccount(auth: Auth): Task<AuthResult>

    fun login(auth: Auth): Task<AuthResult>

    fun updateUserProfile(profileUpdates: UserProfileChangeRequest): Task<Void>?

    fun sendEmailVerification(): Task<Void>?

    fun changePassword(newPassword: String): Task<Void>?

    fun sendResetPasswordEmail(emailAddress: String): Task<Void>?

    fun deleteAccount(): Task<Void>?

    fun reAuthenticate(auth: Auth): Task<Void>?

    fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    fun verifyUserPhoneNumber(phoneAuthCredential: PhoneAuthCredential): Task<Void>?
}