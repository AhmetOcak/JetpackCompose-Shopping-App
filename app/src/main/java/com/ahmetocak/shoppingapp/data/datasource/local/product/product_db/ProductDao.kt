package com.ahmetocak.shoppingapp.data.datasource.local.product.product_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

@Dao
interface ProductDao {

    @Insert
    suspend fun addProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAllProducts(): List<ProductEntity>
}