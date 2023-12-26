package com.ahmetocak.shoppingapp.di

import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.CartLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.FavoriteProductLocalDatasource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.ProductLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.fcm.FirebaseFcmDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.storage.FirebaseStorageDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.store.FirebaseFirestoreDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.domain.repository.FirebaseRepository
import com.ahmetocak.shoppingapp.data.repository.FirebaseRepositoryImpl
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.data.repository.ShoppingRepositoryImpl
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
    fun provideFirebaseRepository(
        authDataSource: FirebaseAuthDataSource,
        storageDataSource: FirebaseStorageDataSource,
        firestoreDataSource: FirebaseFirestoreDataSource,
        fcmDataSource: FirebaseFcmDataSource
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(
            authDataSource,
            storageDataSource,
            firestoreDataSource,
            fcmDataSource
        )
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(
        remoteDataSource: ShoppingRemoteDataSource,
        productLocalDataSource: ProductLocalDataSource,
        favoriteProductLocalDatasource: FavoriteProductLocalDatasource,
        cartLocalDataSource: CartLocalDataSource
    ): ShoppingRepository {
        return ShoppingRepositoryImpl(
            remoteDataSource,
            productLocalDataSource,
            favoriteProductLocalDatasource,
            cartLocalDataSource
        )
    }
}