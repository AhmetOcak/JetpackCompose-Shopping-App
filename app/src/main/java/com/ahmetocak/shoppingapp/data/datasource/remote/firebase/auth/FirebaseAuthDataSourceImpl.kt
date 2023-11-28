package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.auth

import android.app.Activity
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {

    override fun createAccount(auth: Auth) =
        firebaseAuth.createUserWithEmailAndPassword(auth.email, auth.password)

    override fun login(auth: Auth) =
        firebaseAuth.signInWithEmailAndPassword(auth.email, auth.password)

    override fun updateUserProfile(profileUpdates: UserProfileChangeRequest) =
        firebaseAuth.currentUser?.updateProfile(profileUpdates)

    override fun sendEmailVerification() = firebaseAuth.currentUser?.sendEmailVerification()

    override fun changePassword(newPassword: String) =
        firebaseAuth.currentUser?.updatePassword(newPassword)

    override fun sendResetPasswordEmail(emailAddress: String) =
        firebaseAuth.sendPasswordResetEmail(emailAddress)

    override fun deleteAccount() = firebaseAuth.currentUser?.delete()

    override fun reAuthenticate(auth: Auth): Task<Void>? {
        val user = firebaseAuth.currentUser
        val credential = EmailAuthProvider.getCredential(auth.email, auth.password)

        return user?.reauthenticate(credential)
    }

    override fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyUserPhoneNumber(phoneAuthCredential: PhoneAuthCredential): Task<Void>? {
        return firebaseAuth.currentUser?.updatePhoneNumber(phoneAuthCredential)
    }
}