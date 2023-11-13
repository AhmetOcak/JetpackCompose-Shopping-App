package com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

@Dao
interface FavoriteProductDao {

    @Insert
    suspend fun addFavoriteProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAllFavoriteProducts(): List<ProductEntity>

    @Query("SELECT id FROM ProductEntity WHERE id == :productId LIMIT 1")
    suspend fun findFavoriteProduct(productId: Int): ProductEntity?

    @Query("DELETE FROM ProductEntity WHERE id == :productId")
    suspend fun removeFavoriteProduct(productId: Int)
}