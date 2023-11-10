package com.ahmetocak.shoppingapp.data.datasource.local.product.product_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao
}