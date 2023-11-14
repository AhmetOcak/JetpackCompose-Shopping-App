package com.ahmetocak.shoppingapp.model.shopping

data class CreditCard(
    val holderName: String,
    val number: String,
    val expiryDate: String,
    val cvc: String
)
