package com.ahmetocak.shoppingapp.data.repository.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.datasource.local.product.ProductLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShoppingRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource
) : ShoppingRepository {

    override suspend fun getCategories(): Response<List<String>> = remoteDataSource.getCategories()

    override suspend fun getProducts(): Response<List<Product>> = remoteDataSource.getProducts()

    override suspend fun addProduct(productEntity: ProductEntity): Response<Unit> =
        productLocalDataSource.addProduct(productEntity)
}