package com.ahmetocak.shoppingapp.data.repository.firebase

import android.app.Activity
import android.net.Uri
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.model.auth.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseRemoteDataSource
) : FirebaseRepository {

    override fun createAccount(auth: Auth) = dataSource.createAccount(auth)

    override fun login(auth: Auth) = dataSource.login(auth)

    override fun updateUserProfile(profileUpdates: UserProfileChangeRequest) =
        dataSource.updateUserProfile(profileUpdates)

    override fun sendEmailVerification() = dataSource.sendEmailVerification()

    override fun changePassword(newPassword: String) = dataSource.changePassword(newPassword)

    override fun sendResetPasswordEmail(emailAddress: String) =
        dataSource.sendResetPasswordEmail(emailAddress)

    override fun deleteAccount() = dataSource.deleteAccount()

    override fun reAuthenticate(auth: Auth) = dataSource.reAuthenticate(auth)

    override fun uploadUserProfileImage(imgUri: Uri) = dataSource.uploadUserProfileImage(imgUri)

    override fun getUserProfileImage() = dataSource.getUserProfileImage()

    override fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        dataSource.sendVerificationCode(phoneNumber, activity, callbacks)
    }

    override fun verifyUserPhoneNumber(phoneAuthCredential: PhoneAuthCredential): Task<Void>? {
        return dataSource.verifyUserPhoneNumber(phoneAuthCredential)
    }

    override fun uploadUserAddress(address: String, userUid: String): Task<Void> {
        return dataSource.uploadUserAddress(address, userUid)
    }
}