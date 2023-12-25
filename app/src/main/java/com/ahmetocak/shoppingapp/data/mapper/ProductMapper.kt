package com.ahmetocak.shoppingapp.data.mapper

import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.model.shopping.Rating

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        image = this.image,
        rating = this.rating?.rate,
        count = this.rating?.count
    )
}

fun ProductEntity.toProduct(): Product {
    return Product(
        id,
        title,
        price,
        description,
        category,
        image,
        Rating(
            rating,
            count
        )
    )
}