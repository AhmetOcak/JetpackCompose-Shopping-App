package com.ahmetocak.shoppingapp.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.utils.NavKeys
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenUiState())
    val uiState: StateFlow<ProductScreenUiState> = _uiState.asStateFlow()

    init {
        val arg = savedStateHandle.get<String>(NavKeys.PRODUCT)
        val decodedValue = URLDecoder.decode(arg, "UTF-8")
        _uiState.update {
            it.copy(product = Gson().fromJson(decodedValue, Product::class.java))
        }
    }
}

data class ProductScreenUiState(
    val product: Product? = null
)