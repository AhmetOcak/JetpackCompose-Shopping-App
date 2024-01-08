package com.ahmetocak.shoppingapp.data.datasource.remote.firebase.store

import com.ahmetocak.shoppingapp.utils.Firestore
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class FirebaseFirestoreDataSourceImpl @Inject constructor(
    private val firestoreDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseFirestoreDataSource {

    override fun uploadUserAddress(address: String): Task<Void> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(hashMapOf("address" to address), SetOptions.merge())
    }

    override fun uploadUserBirthdate(birthdate: Long): Task<Void> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(hashMapOf("birthdate" to birthdate), SetOptions.merge())
    }

    override fun getAllUserDetails(): Task<DocumentSnapshot> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "").get()
    }

    override fun uploadUserFCMToken(token: String): Task<Void> {
        return firestoreDb.collection(Firestore.FCM_COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(hashMapOf("token" to token), SetOptions.merge())
    }

    override fun deleteUserFCMToken(): Task<Void> {
        return firestoreDb.collection(Firestore.FCM_COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .delete()
    }

    override fun deleteUserData(): Task<Void> {
        return firestoreDb.collection(Firestore.COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .delete()
    }
}