package com.ahmetocak.shoppingapp.di

import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.CartLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.CartLocalDataSourceImpl
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.db.CartDao
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.FavoriteLocalDatasourceImpl
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.FavoriteProductLocalDatasource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.db.FavoriteProductDao
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.ProductLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.ProductLocalDataSourceImpl
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.db.ProductDao
import com.ahmetocak.shoppingapp.data.datasource.remote.api.ShoppingApi
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDatasourceImpl
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
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
    fun provideFirebaseRemoteDataSource(
        auth: FirebaseAuth,
        storage: FirebaseStorage,
        db: FirebaseFirestore,
        messaging: FirebaseMessaging
    ): FirebaseRemoteDataSource {
        return FirebaseRemoteDatasourceImpl(auth, storage, db, messaging)
    }

    @Provides
    @Singleton
    fun provideShoppingRemoteDataSource(api: ShoppingApi): ShoppingRemoteDataSource {
        return ShoppingRemoteDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun provideLocalProductDataSource(productDao: ProductDao): ProductLocalDataSource {
        return ProductLocalDataSourceImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteProductLocalDataSource(favoriteProductDao: FavoriteProductDao): FavoriteProductLocalDatasource {
        return FavoriteLocalDatasourceImpl(favoriteProductDao)
    }

    @Provides
    @Singleton
    fun provideCartLocalDataSource(cartDao: CartDao): CartLocalDataSource {
        return CartLocalDataSourceImpl(cartDao)
    }
}