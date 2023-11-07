package com.ahmetocak.shoppingapp.di

import com.ahmetocak.shoppingapp.data.datasource.remote.api.ShoppingApi
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDatasourceImpl
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFirebaseRemoteDataSource(auth: FirebaseAuth, storage: FirebaseStorage): FirebaseRemoteDataSource {
        return FirebaseRemoteDatasourceImpl(auth, storage)
    }

    @Provides
    @Singleton
    fun provideShoppingRemoteDataSource(api: ShoppingApi): ShoppingRemoteDataSource {
        return ShoppingRemoteDataSourceImpl(api)
    }
}