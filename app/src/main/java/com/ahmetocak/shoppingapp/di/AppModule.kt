package com.ahmetocak.shoppingapp.di

import android.content.Context
import android.content.SharedPreferences
import com.ahmetocak.shoppingapp.BuildConfig
import com.ahmetocak.shoppingapp.utils.USER_REMEMBER_ME_KEY
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences(USER_REMEMBER_ME_KEY, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage() : FirebaseStorage {
        return Firebase.storage
    }

    @Provides
    @Singleton
    fun provideFirestore(@ApplicationContext context: Context) : FirebaseFirestore {
        val options = FirebaseOptions.Builder()
            .setProjectId(BuildConfig.PROJECT_ID)
            .setApplicationId(BuildConfig.APPLICATION_ID)
            .setApiKey(BuildConfig.API_KEY)
            .setStorageBucket(BuildConfig.STORAGE_BUCKET)
            .build()

        Firebase.initialize(context, options, "secondary")
        val secondary = Firebase.app("secondary")

        return FirebaseFirestore.getInstance(secondary)
    }

    @Provides
    @Singleton
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}