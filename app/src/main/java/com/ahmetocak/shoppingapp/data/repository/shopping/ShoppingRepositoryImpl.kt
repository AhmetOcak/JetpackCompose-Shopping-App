package com.ahmetocak.shoppingapp.data.repository.shopping

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.FavoriteProductLocalDatasource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.ProductLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShoppingRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
    private val favoriteProductLocalDatasource: FavoriteProductLocalDatasource
) : ShoppingRepository {

    override suspend fun getCategories(): Response<List<String>> = remoteDataSource.getCategories()

    override suspend fun getProducts(): Response<List<Product>> = remoteDataSource.getProducts()

    override suspend fun addProduct(productEntity: ProductEntity): Response<Unit> =
        productLocalDataSource.addProduct(productEntity)

    override suspend fun getAllProducts(): Response<List<ProductEntity>> =
        productLocalDataSource.getAllProducts()

    override suspend fun addFavoriteProduct(productEntity: ProductEntity): Response<Unit> =
        favoriteProductLocalDatasource.addFavoriteProduct(productEntity)

    override suspend fun getAllFavoriteProducts(): Response<List<ProductEntity>> =
        favoriteProductLocalDatasource.getAllFavoriteProducts()

    override suspend fun findFavoriteProduct(productId: Int): Response<ProductEntity?> =
        favoriteProductLocalDatasource.findFavoriteProduct(productId)

    override suspend fun removeFavoriteProduct(productId: Int): Response<Unit> =
        favoriteProductLocalDatasource.removeFavoriteProduct(productId)
}