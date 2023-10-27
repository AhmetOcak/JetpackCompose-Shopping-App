package com.ahmetocak.shoppingapp.di

import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDatasourceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteDataSource(auth: FirebaseAuth): FirebaseRemoteDataSource {
        return FirebaseRemoteDatasourceImpl(auth)
    }
}