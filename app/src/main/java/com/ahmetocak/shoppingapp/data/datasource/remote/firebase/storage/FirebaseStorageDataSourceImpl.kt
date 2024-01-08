package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.storage

import android.net.Uri
import com.ahmetocak.shoppingapp.utils.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class FirebaseStorageDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    firebaseStorage: FirebaseStorage
) : FirebaseStorageDataSource {

    private val storageRef = firebaseStorage.reference

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

    override fun deleteUserProfileImage(): Task<Void> {
        return storageRef.child("${Storage.USER_PROFILE_IMG}/${firebaseAuth.currentUser?.uid}")
            .delete()
    }
}