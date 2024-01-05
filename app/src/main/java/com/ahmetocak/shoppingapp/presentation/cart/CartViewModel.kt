package com.ahmetocak.shoppingapp.presentation.cart

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class CartViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository,
    private val ioDispatcher:CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()

    init {
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.getCart()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            cartList = response.data,
                            subtotal = calculateSubtotal(response.data)
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(resId = response.errorMessageId)
                        ))
                    }
                }
            }
        }
    }

    fun removeProductFromCart(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.removeProductFromCart(productId)) {
                is Response.Success -> {
                    val cartList = _uiState.value.cartList.toMutableList()
                    cartList.removeIf { it.id == productId }

                    _uiState.update {
                        it.copy(cartList = cartList, subtotal = calculateSubtotal(cartList))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(resId = response.errorMessageId)
                        ))
                    }
                }
            }
        }
    }

    fun increaseProductCount(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.increaseCartItemCount(productId)) {
                is Response.Success -> {
                    getCart()
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(resId = response.errorMessageId)
                        ))
                    }
                }
            }
        }
    }

    fun decreaseProductCount(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.decreaseCartItemCount(productId)) {
                is Response.Success -> {
                    getCart()
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(resId = response.errorMessageId)
                        ))
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

    private fun calculateSubtotal(cartList: List<CartEntity>): Double {
        var subtotal = 0.0
        cartList.forEach {
            subtotal += it.price * it.count
        }
        return subtotal
    }
}

data class CartScreenUiState(
    val cartList: List<CartEntity> = listOf(),
    val errorMessages: List<UiText> = listOf(),
    val subtotal: Double = 0.0
)