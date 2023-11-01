package com.ahmetocak.shoppingapp.common.helpers

import android.util.Log
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.ErrorType
import com.ahmetocak.shoppingapp.common.HomeScreenError
import com.ahmetocak.shoppingapp.common.Response
import java.io.IOException

suspend inline fun <T> apiCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: IOException) {
        Response.Error(
            error = HomeScreenError(
                messageId = R.string.network,
                type = ErrorType.CategoryList
            )
        )
    } catch (e: Exception) {
        Log.d("API CALL EXCEPTION", e.message.toString())
        Response.Error(
            error = HomeScreenError(
                messageId = R.string.unknown_error,
                type = ErrorType.CategoryList
            )
        )
    }
}