package com.ahmetocak.shoppingapp.data.repository.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val dataSource: ShoppingRemoteDataSource
) : ShoppingRepository {

    override suspend fun getCategories(): Response<List<String>> = dataSource.getCategories()
}