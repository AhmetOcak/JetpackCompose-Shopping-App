package com.ahmetocak.shoppingapp.data.datasource.remote.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.Product

interface ShoppingRemoteDataSource {

    suspend fun getCategories(): Response<List<String>>

    suspend fun getProducts(): Response<List<Product>>
}