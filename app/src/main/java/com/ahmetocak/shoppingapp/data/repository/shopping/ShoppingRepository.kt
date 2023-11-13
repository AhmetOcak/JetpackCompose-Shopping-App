package com.ahmetocak.shoppingapp.data.repository.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

interface ShoppingRepository {

    suspend fun getCategories(): Response<List<String>>

    suspend fun getProducts(): Response<List<Product>>

    suspend fun addProduct(productEntity: ProductEntity): Response<Unit>

    suspend fun getAllProducts(): Response<List<ProductEntity>>

    suspend fun addFavoriteProduct(productEntity: ProductEntity): Response<Unit>

    suspend fun getAllFavoriteProducts(): Response<List<ProductEntity>>

    suspend fun findFavoriteProduct(productId: Int): Response<ProductEntity?>

    suspend fun removeFavoriteProduct(productId: Int): Response<Unit>
}