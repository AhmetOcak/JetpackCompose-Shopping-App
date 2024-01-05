package com.ahmetocak.shoppingapp.model.shopping

import androidx.compose.runtime.Immutable

@Immutable
data class Product(
    val id: Int,
    val title: String? = null,
    val price: String? = null,
    val description: String? = null,
    val category: String? = null,
    val image: String? = null,
    val rating: Rating? = null
)

@Immutable
data class Rating(
    val rate: Double? = null,
    val count: Int? = null
)