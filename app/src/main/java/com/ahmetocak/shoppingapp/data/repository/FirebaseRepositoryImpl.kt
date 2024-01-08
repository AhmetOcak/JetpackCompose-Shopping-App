package com.ahmetocak.shoppingapp.data.repository

import android.app.Activity
import android.net.Uri
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.fcm.FirebaseFcmDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.storage.FirebaseStorageDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.store.FirebaseFirestoreDataSource
import com.ahmetocak.shoppingapp.domain.repository.FirebaseRepository
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val storageDataSource: FirebaseStorageDataSource,
    private val firestoreDataSource: FirebaseFirestoreDataSource,
    private val fcmDataSource: FirebaseFcmDataSource
) : FirebaseRepository {

    override fun createAccount(auth: Auth) = authDataSource.createAccount(auth)

    override fun login(auth: Auth) = authDataSource.login(auth)

    override fun updateUserProfile(profileUpdates: UserProfileChangeRequest) =
        authDataSource.updateUserProfile(profileUpdates)

    override fun sendEmailVerification() = authDataSource.sendEmailVerification()

    override fun changePassword(newPassword: String) = authDataSource.changePassword(newPassword)

    override fun sendResetPasswordEmail(emailAddress: String) =
        authDataSource.sendResetPasswordEmail(emailAddress)

    override fun deleteAccount() = authDataSource.deleteAccount()

    override fun reAuthenticate(auth: Auth) = authDataSource.reAuthenticate(auth)

    override fun uploadUserProfileImage(imgUri: Uri) = storageDataSource.uploadUserProfileImage(imgUri)

    override fun getUserProfileImage() = storageDataSource.getUserProfileImage()

    override fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        authDataSource.sendVerificationCode(phoneNumber, activity, callbacks)
    }

    override fun verifyUserPhoneNumber(phoneAuthCredential: PhoneAuthCredential): Task<Void>? {
        return authDataSource.verifyUserPhoneNumber(phoneAuthCredential)
    }

    override fun uploadUserAddress(address: String): Task<Void> {
        return firestoreDataSource.uploadUserAddress(address)
    }

    override fun uploadUserBirthdate(birthdate: Long): Task<Void> {
        return firestoreDataSource.uploadUserBirthdate(birthdate)
    }

    override fun getAllUserDetails(): Task<DocumentSnapshot> {
        return firestoreDataSource.getAllUserDetails()
    }

    override fun uploadUserFCMToken(token: String): Task<Void> {
        return firestoreDataSource.uploadUserFCMToken(token)
    }

    override fun getFCMToken(): Task<String> {
        return fcmDataSource.getFCMToken()
    }

    override fun deleteUserFCMToken(): Task<Void> {
        return firestoreDataSource.deleteUserFCMToken()
    }

    override fun deleteUserProfileImage(): Task<Void> {
        return storageDataSource.deleteUserProfileImage()
    }

    override fun deleteUserData(): Task<Void> {
        return firestoreDataSource.deleteUserData()
    }
}