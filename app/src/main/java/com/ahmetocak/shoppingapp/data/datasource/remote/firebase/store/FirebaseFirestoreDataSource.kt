package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.store

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface FirebaseFirestoreDataSource {

    fun uploadUserAddress(address: String, userUid: String): Task<Void>

    fun uploadUserBirthdate(birthdate: Long, userUid: String): Task<Void>

    fun getAllUserDetails(userUid: String): Task<DocumentSnapshot>

    fun uploadUserFCMToken(token: String, userUid: String): Task<Void>
}