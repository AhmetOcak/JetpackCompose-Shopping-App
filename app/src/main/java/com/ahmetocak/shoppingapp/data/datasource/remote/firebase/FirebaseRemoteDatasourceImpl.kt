package com.ahmetocak.shoppingapp.data.datasource.remote.firebase

import android.app.Activity
import android.net.Uri
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.ahmetocak.shoppingapp.utils.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseRemoteDatasourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    firebaseStorage: FirebaseStorage
) : FirebaseRemoteDataSource {

    private val storageRef = firebaseStorage.reference

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

    override fun uploadUserProfileImage(imgUri: Uri): UploadTask {
        val profileImagesRef =
            storageRef.child("${Storage.USER_PROFILE_IMG}/${firebaseAuth.currentUser?.uid}")

        return profileImagesRef.putFile(imgUri)
    }

    override fun getUserProfileImage(): Task<Uri> {
        val profileImagesRef =
            storageRef.child("${Storage.USER_PROFILE_IMG}/${firebaseAuth.currentUser?.uid}")

        return profileImagesRef.downloadUrl
    }

    override fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: OnVerificationStateChangedCallbacks
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