package com.ahmetocak.shoppingapp.di

import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseRepository(dataSource: FirebaseRemoteDataSource) : FirebaseRepository {
        return FirebaseRepositoryImpl(dataSource)
    }
}