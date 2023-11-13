package com.ahmetocak.shoppingapp.data.datasource.local.shopping.product

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

interface ProductLocalDataSource {

    suspend fun addProduct(productEntity: ProductEntity): Response<Unit>

    suspend fun getAllProducts(): Response<List<ProductEntity>>
}