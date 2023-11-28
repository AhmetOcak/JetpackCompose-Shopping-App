package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.store

import com.ahmetocak.shoppingapp.utils.Firestore
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class FirebaseFirestoreDataSourceImpl @Inject constructor(
    private val firestoreDb: FirebaseFirestore
) : FirebaseFirestoreDataSource {

    override fun uploadUserAddress(address: String, userUid: String): Task<Void> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY)
            .document(userUid)
            .set(hashMapOf("address" to address), SetOptions.merge())
    }

    override fun uploadUserBirthdate(birthdate: Long, userUid: String): Task<Void> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY)
            .document(userUid)
            .set(hashMapOf("birthdate" to birthdate), SetOptions.merge())
    }

    override fun getAllUserDetails(userUid: String): Task<DocumentSnapshot> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY).document(userUid).get()
    }

    override fun uploadUserFCMToken(token: String, userUid: String): Task<Void> {
        return firestoreDb.collection(Firestore.FCM_COLLECTION_KEY)
            .document(userUid)
            .set(hashMapOf("token" to token), SetOptions.merge())
    }
}