package com.ahmetocak.shoppingapp.data.repository

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.cart.CartLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.favorite_product.FavoriteProductLocalDatasource
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.ProductLocalDataSource
import com.ahmetocak.shoppingapp.data.datasource.remote.shopping.ShoppingRemoteDataSource
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShoppingRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
    private val favoriteProductLocalDatasource: FavoriteProductLocalDatasource,
    private val cartLocalDataSource: CartLocalDataSource
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

    override suspend fun addProductToCart(cartEntity: CartEntity): Response<Unit> =
        cartLocalDataSource.addProductToCart(cartEntity)

    override suspend fun removeProductFromCart(productId: Int): Response<Unit> =
        cartLocalDataSource.removeProductFromCart(productId)

    override suspend fun getCart(): Response<List<CartEntity>> =
        cartLocalDataSource.getCart()

    override suspend fun findCartItem(productId: Int): Response<CartEntity?> =
        cartLocalDataSource.findCartItem(productId)

    override suspend fun increaseCartItemCount(cartItemId: Int): Response<Unit> =
        cartLocalDataSource.increaseCartItemCount(cartItemId)

    override suspend fun decreaseCartItemCount(cartItemId: Int): Response<Unit> =
        cartLocalDataSource.decreaseCartItemCount(cartItemId)

    override suspend fun deleteAllCartItems(): Response<Unit> =
        cartLocalDataSource.deleteAllItemsFromCart()
}