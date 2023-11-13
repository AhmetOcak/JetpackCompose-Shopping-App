package com.ahmetocak.shoppingapp.model.shopping

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("price")
    val price: Double,

    @ColumnInfo("image")
    val image: String,

    @ColumnInfo("count")
    val count: Int
)
