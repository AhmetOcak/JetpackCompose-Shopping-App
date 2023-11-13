package com.ahmetocak.shoppingapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()

    init {
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.getCart()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(cartList = response.data)
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(response.errorMessageId))
                    }
                }
            }
        }
    }

    fun removeProductFromCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.removeProductFromCart(productId)) {
                is Response.Success -> {
                    val cartList = _uiState.value.cartList.toMutableList()
                    cartList.removeIf { it.id == productId }

                    _uiState.update {
                        it.copy(cartList = cartList)
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(response.errorMessageId))
                    }
                }
            }
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = listOf())
        }
    }
}

data class CartScreenUiState(
    val cartList: List<CartEntity> = listOf(),
    val userMessages: List<Int> = listOf(),
    val errorMessages: List<Int> = listOf()
)