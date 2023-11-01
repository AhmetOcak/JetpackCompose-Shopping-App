package com.ahmetocak.shoppingapp.data.datasource.remote.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.apiCall
import com.ahmetocak.shoppingapp.data.datasource.remote.api.ShoppingApi
import javax.inject.Inject

class ShoppingRemoteDataSourceImpl @Inject constructor(
    private val api: ShoppingApi
): ShoppingRemoteDataSource {
    override suspend fun getCategories(): Response<List<String>> = apiCall { api.getCategories() }
}