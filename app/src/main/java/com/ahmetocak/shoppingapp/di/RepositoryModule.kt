package com.ahmetocak.shoppingapp.di

import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.CartLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.FavoriteProductLocalDatasource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.ProductLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.firebase.FirebaseRemoteDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.shoppingapp.data.repository.firebase.FirebaseRepositoryImpl
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepositoryImpl
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
    fun provideFirebaseRepository(dataSource: FirebaseRemoteDataSource): FirebaseRepository {
        return FirebaseRepositoryImpl(dataSource)
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