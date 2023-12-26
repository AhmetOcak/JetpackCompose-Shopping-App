package com.ahmetocak.shoppingapp.domain.repository

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

interface ShoppingRepository {

    suspend fun getCategories(): Response<List<String>>

    suspend fun getProducts(): Response<List<Product>>

    suspend fun addProduct(productEntity: ProductEntity): Response<Unit>

    suspend fun getAllProducts(): Response<List<ProductEntity>>

    suspend fun addFavoriteProduct(productEntity: ProductEntity): Response<Unit>

    suspend fun getAllFavoriteProducts(): Response<List<ProductEntity>>

    suspend fun findFavoriteProduct(productId: Int): Response<ProductEntity?>

    suspend fun removeFavoriteProduct(productId: Int): Response<Unit>

    suspend fun addProductToCart(cartEntity: CartEntity): Response<Unit>

    suspend fun removeProductFromCart(productId: Int): Response<Unit>

    suspend fun getCart(): Response<List<CartEntity>>

    suspend fun findCartItem(productId: Int): Response<CartEntity?>

    suspend fun increaseCartItemCount(cartItemId: Int): Response<Unit>

    suspend fun decreaseCartItemCount(cartItemId: Int): Response<Unit>

    suspend fun deleteAllCartItems(): Response<Unit>
}