package com.ahmetocak.shoppingapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreeUiState())
    val uiState: StateFlow<HomeScreeUiState> = _uiState.asStateFlow()

    init {
        getCategoryList()
        getAllProducts()
    }

    private fun getCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.getCategories()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            isCategoryListLoading = false,
                            categoryList = response.data
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            isCategoryListLoading = false,
                            errorMessages = listOf(response.errorMessageId)
                        )
                    }
                }
            }
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = shoppingRepository.getProducts()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            isProductListLoading = false,
                            productList = response.data
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            isProductListLoading = false,
                            errorMessages = listOf(response.errorMessageId)
                        )
                    }
                }
            }
        }
    }
}

data class HomeScreeUiState(
    val isCategoryListLoading: Boolean = true,
    val isProductListLoading: Boolean = true,
    val categoryList: List<String> = listOf(),
    val productList: List<Product> = listOf(),
    val errorMessages: List<Int> = listOf()
)