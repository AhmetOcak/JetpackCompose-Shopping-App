package com.ahmetocak.shoppingapp.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.mapper.toProductEntity
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.utils.NavKeys
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenUiState())
    val uiState: StateFlow<ProductScreenUiState> = _uiState.asStateFlow()

    init {
        val arg = savedStateHandle.get<String>(NavKeys.PRODUCT)
        val decodedValue = URLDecoder.decode(arg?.replace("%", "%25"), StandardCharsets.UTF_8.name())
        _uiState.update {
            it.copy(product = Gson().fromJson(decodedValue, Product::class.java))
        }
        _uiState.value.product?.id?.let { findProduct(productId = it) }
        _uiState.value.product?.id?.let { findProductInCart(productId = it) }
    }

    private fun findProduct(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.findFavoriteProduct(productId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(isProductFavorite = response.data != null)
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

    fun onFavoriteProductClick() {
        if (_uiState.value.isProductFavorite) {
            removeProductFromFavorites()
        } else {
            addProductToFavorites()
        }
    }

    private fun addProductToFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response =
                _uiState.value.product?.toProductEntity()
                    ?.let { shoppingRepository.addFavoriteProduct(it) }
            ) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(R.string.product_added_favorites),
                            isProductFavorite = true
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(response.errorMessageId))
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(R.string.unknown_error))
                    }
                }
            }
        }
    }

    private fun removeProductFromFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response =
                _uiState.value.product?.id?.let { shoppingRepository.removeFavoriteProduct(it) }
            ) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(R.string.product_removed_favorites),
                            isProductFavorite = false
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(response.errorMessageId))
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(R.string.unknown_error))
                    }
                }
            }
        }
    }

    fun addProductToCart() {
        val product = _uiState.value.product

        if (product?.id != null && product.title != null && product.price != null && product.image != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = shoppingRepository.addProductToCart(
                    CartEntity(
                        id = product.id,
                        title = product.title,
                        price = product.price.toDouble(),
                        image = product.image,
                        count = 1
                    )
                )) {
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                userMessages = listOf(R.string.product_added_cart),
                                isProductInCart = true
                            )
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
    }

    private fun findProductInCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.findCartItem(productId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(isProductInCart = response.data != null)
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

    fun consumedUserMessages() {
        _uiState.update {
            it.copy(userMessages = listOf())
        }
    }

    fun consumedErrorMessages() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class ProductScreenUiState(
    val product: Product? = null,
    val isProductFavorite: Boolean = false,
    val userMessages: List<Int> = listOf(),
    val errorMessages: List<Int> = listOf(),
    val isProductInCart: Boolean = false
)