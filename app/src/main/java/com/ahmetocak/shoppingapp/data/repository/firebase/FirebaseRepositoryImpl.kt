package com.ahmetocak.shoppingapp.data.repository.firebase

import android.net.Uri
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.model.auth.Auth
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
}