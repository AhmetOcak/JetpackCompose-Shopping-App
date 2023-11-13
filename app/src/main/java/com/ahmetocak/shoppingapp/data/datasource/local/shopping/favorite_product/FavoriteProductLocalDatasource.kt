package com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

interface FavoriteProductLocalDatasource {

    suspend fun addFavoriteProduct(productEntity: ProductEntity): Response<Unit>

    suspend fun getAllFavoriteProducts(): Response<List<ProductEntity>>

    suspend fun findFavoriteProduct(productId: Int): Response<ProductEntity?>

    suspend fun removeFavoriteProduct(productId: Int): Response<Unit>
}