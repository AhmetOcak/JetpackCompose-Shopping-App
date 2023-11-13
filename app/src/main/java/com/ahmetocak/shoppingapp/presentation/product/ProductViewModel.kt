package com.ahmetocak.shoppingapp.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.mapper.toProductEntity
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
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
        val decodedValue = URLDecoder.decode(arg, "UTF-8")
        _uiState.update {
            it.copy(product = Gson().fromJson(decodedValue, Product::class.java))
        }
        _uiState.value.product?.id?.let { findProduct(productId = it) }
    }

    private fun findProduct(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.findFavoriteProduct(productId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            isProductFavorite = response.data != null,
                            favoriteBtnEnabled = true
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(favoriteBtnEnabled = false)
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
    val favoriteBtnEnabled: Boolean = true,
    val userMessages: List<Int> = listOf(),
    val errorMessages: List<Int> = listOf()
)