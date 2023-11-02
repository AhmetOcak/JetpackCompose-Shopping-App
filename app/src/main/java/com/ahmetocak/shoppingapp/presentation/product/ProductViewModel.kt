package com.ahmetocak.shoppingapp.presentation.product

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.utils.NavKeys
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val arg = savedStateHandle.get<String>(NavKeys.PRODUCT)
        val product = Gson().fromJson(arg, Product::class.java)
        Log.d("PRODUCT", product.toString())
    }
}