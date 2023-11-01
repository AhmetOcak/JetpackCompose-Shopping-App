package com.ahmetocak.shoppingapp.data.repository.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.Product

interface ShoppingRepository {

    suspend fun getCategories(): Response<List<String>>

    suspend fun getProducts(): Response<List<Product>>
}