package com.ahmetocak.shoppingapp.data.datasource.remote.firebase

import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class FirebaseRemoteDatasourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseRemoteDataSource {

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
}