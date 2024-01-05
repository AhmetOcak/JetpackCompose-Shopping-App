package com.ahmetocak.shoppingapp.presentation.product_detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.presentation.navigation.MainDestinations
import com.ahmetocak.shoppingapp.data.mapper.toProductEntity
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@Stable
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val shoppingRepository: ShoppingRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenUiState())
    val uiState: StateFlow<ProductScreenUiState> = _uiState.asStateFlow()

    init {
        val arg = savedStateHandle.get<String>(MainDestinations.PRODUCT_DETAIL_KEY)
        val decodedValue = URLDecoder.decode(arg?.replace("%", "%25"), StandardCharsets.UTF_8.name())
        _uiState.update {
            it.copy(product = Gson().fromJson(decodedValue, Product::class.java))
        }
        _uiState.value.product?.id?.let { findProduct(productId = it) }
        _uiState.value.product?.id?.let { findProductInCart(productId = it) }
    }

    private fun findProduct(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.findFavoriteProduct(productId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(isProductFavorite = response.data != null)
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
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
        viewModelScope.launch(ioDispatcher) {
            when (val response =
                _uiState.value.product?.toProductEntity()
                    ?.let { shoppingRepository.addFavoriteProduct(it) }
            ) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(
                                UiText.StringResource(R.string.product_added_favorites)
                            ),
                            isProductFavorite = true
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(R.string.unknown_error)
                        ))
                    }
                }
            }
        }
    }

    private fun removeProductFromFavorites() {
        viewModelScope.launch(ioDispatcher) {
            when (val response =
                _uiState.value.product?.id?.let { shoppingRepository.removeFavoriteProduct(it) }
            ) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(
                                UiText.StringResource(R.string.product_removed_favorites)
                            ),
                            isProductFavorite = false
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(R.string.unknown_error)
                        ))
                    }
                }
            }
        }
    }

    fun addProductToCart() {
        val product = _uiState.value.product

        if (product?.id != null && product.title != null && product.price != null && product.image != null) {
            viewModelScope.launch(ioDispatcher) {
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
                                userMessages = listOf(
                                    UiText.StringResource(R.string.product_added_cart)
                                ),
                                isProductInCart = true
                            )
                        }
                    }
                    is Response.Error -> {
                        _uiState.update {
                            it.copy(errorMessages = listOf(
                                UiText.StringResource(response.errorMessageId)
                            ))
                        }
                    }
                }
            }
        }
    }

    private fun findProductInCart(productId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.findCartItem(productId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(isProductInCart = response.data != null)
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(
                            UiText.StringResource(response.errorMessageId)
                        ))
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
    val userMessages: List<UiText> = listOf(),
    val errorMessages: List<UiText> = listOf(),
    val isProductInCart: Boolean = false
)