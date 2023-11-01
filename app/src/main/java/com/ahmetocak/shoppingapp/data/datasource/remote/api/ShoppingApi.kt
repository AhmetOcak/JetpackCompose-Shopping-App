package com.ahmetocak.shoppingapp.data.datasource.remote.api

import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.utils.EndPoints
import retrofit2.http.GET

interface ShoppingApi {

    @GET(EndPoints.CATEGORY)
    suspend fun getCategories(): ArrayList<String>

    @GET(EndPoints.PRODUCT)
    suspend fun getProducts(): List<Product>
}