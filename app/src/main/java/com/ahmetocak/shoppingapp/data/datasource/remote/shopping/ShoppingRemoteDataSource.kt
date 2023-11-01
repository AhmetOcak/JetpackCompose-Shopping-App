package com.ahmetocak.shoppingapp.data.datasource.remote.shopping

import com.ahmetocak.shoppingapp.common.Response

interface ShoppingRemoteDataSource {

    suspend fun getCategories() : Response<List<String>>
}