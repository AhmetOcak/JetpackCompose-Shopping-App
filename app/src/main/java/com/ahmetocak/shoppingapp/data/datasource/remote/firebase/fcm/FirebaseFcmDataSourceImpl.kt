package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.fcm

import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class FirebaseFcmDataSourceImpl @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) : FirebaseFcmDataSource {

    override fun getFCMToken(): Task<String> {
        return firebaseMessaging.token
    }
}