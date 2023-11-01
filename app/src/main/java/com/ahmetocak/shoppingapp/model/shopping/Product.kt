package com.ahmetocak.shoppingapp.model.shopping

data class Product(
    val id: Int? = null,
    val title: String? = null,
    val price: String? = null,
    val description: String? = null,
    val category: String? = null,
    val image: String? = null,
    val rating: Rating? = null
)

data class Rating(
    val rate: Double? = null,
    val count: Int? = null
)