package com.ahmetocak.shoppingapp.common

interface Error {
    val errorMessageId: Int
    val errorType: ErrorType
}

sealed interface ErrorType {

    object CategoryList : ErrorType
}

class HomeScreenError(private val messageId: Int, private val type: ErrorType) : Error {
    override val errorMessageId: Int
        get() = messageId
    override val errorType: ErrorType
        get() = type
}