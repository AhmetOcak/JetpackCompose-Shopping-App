package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.store

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface FirebaseFirestoreDataSource {

    fun uploadUserAddress(address: String): Task<Void>

    fun uploadUserBirthdate(birthdate: Long): Task<Void>

    fun getAllUserDetails(): Task<DocumentSnapshot>

    fun uploadUserFCMToken(token: String): Task<Void>

    fun deleteUserFCMToken(): Task<Void>

    fun deleteUserData(): Task<Void>
}