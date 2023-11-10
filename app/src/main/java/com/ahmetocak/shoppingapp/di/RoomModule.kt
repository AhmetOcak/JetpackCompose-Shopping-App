package com.ahmetocak.shoppingapp.di

import android.content.Context
import androidx.room.Room
import com.ahmetocak.shoppingapp.data.datasource.local.product.product_db.ProductDao
import com.ahmetocak.shoppingapp.data.datasource.local.product.product_db.ProductDatabase
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
    fun provideProductDao(db: ProductDatabase) : ProductDao {
        return db.productDao()
    }
}