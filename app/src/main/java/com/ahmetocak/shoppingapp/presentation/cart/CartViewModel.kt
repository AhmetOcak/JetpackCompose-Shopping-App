package com.ahmetocak.shoppingapp.presentation.cart

import androidx.lifecycle.ViewModel
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {
}

data class ChartScreenUiState(
    val cartList: List<CartEntity> = listOf(),
    val userMessages: List<Int> = listOf(),
    val errorMessages: List<Int> = listOf()
)