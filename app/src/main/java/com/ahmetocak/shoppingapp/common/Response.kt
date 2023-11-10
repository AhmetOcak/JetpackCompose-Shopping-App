package com.ahmetocak.shoppingapp.common

sealed interface Response<out T> {
    class Success<T>(val data: T) : Response<T>
    class Error<T>(val errorMessageId: Int) : Response<T>
}