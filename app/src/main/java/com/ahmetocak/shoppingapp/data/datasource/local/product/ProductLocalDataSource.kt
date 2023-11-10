package com.ahmetocak.shoppingapp.data.datasource.local.product

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

interface ProductLocalDataSource {

    suspend fun addProduct(productEntity: ProductEntity) : Response<Unit>
}