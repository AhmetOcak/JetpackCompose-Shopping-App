package com.ahmetocak.shoppingapp.di

import android.content.Context
import androidx.room.Room
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.db.CartDao
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.db.CartDatabase
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.db.FavoriteProductDao
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.db.FavoriteProductDatabase
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.db.ProductDao
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.db.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideProductDb(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "product_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(db: ProductDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteProductDb(@ApplicationContext context: Context): FavoriteProductDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteProductDatabase::class.java,
            "favorite_product_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteProductDao(db: FavoriteProductDatabase): FavoriteProductDao {
        return db.favoriteProductDao()
    }

    @Provides
    @Singleton
    fun provideCartDb(@ApplicationContext context: Context): CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "card_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCardDao(db: CartDatabase): CartDao {
        return db.cartDao()
    }
}