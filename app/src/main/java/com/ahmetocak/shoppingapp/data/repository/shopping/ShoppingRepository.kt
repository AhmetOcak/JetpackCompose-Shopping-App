package com.ahmetocak.shoppingapp.data.repository.shopping

import com.ahmetocak.shoppingapp.common.Response

interface ShoppingRepository {

    suspend fun getCategories(): Response<List<String>>
}