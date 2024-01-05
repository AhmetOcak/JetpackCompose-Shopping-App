package com.ahmetocak.shoppingapp.model.shopping

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity
data class ProductEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("title")
    val title: String?,

    @ColumnInfo("price")
    val price: String?,

    @ColumnInfo("description")
    val description: String?,

    @ColumnInfo("category")
    val category: String?,

    @ColumnInfo("image")
    val image: String?,

    @ColumnInfo("rate")
    val rating: Double?,

    @ColumnInfo("count")
    val count: Int?
)