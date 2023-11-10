package com.ahmetocak.shoppingapp.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    init {
        getAllProducts()
    }

    var searchedText by mutableStateOf("")
        private set

    fun updateSearchedText(value: String) {
        searchedText = value
    }

    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repository.getAllProducts()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(productList = response.data)
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

    fun searchProductList() {
        val searchedResults = _uiState.value.productList.filter {
            it.title?.contains(searchedText) == true
        }

        _uiState.update {
            it.copy(
                searchResult = if (searchedText.isNotBlank()) searchedResults else listOf(),
                isSearchResultEmpty = searchedResults.isEmpty() && searchedText.isNotBlank()
            )
        }
    }

    fun errorConsumed() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class SearchScreenUiState(
    val productList: List<ProductEntity> = listOf(),
    val searchResult: List<ProductEntity> = listOf(),
    val errorMessages: List<Int> = listOf(),
    val isSearchResultEmpty: Boolean = false
)