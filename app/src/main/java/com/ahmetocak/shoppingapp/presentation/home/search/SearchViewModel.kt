package com.ahmetocak.shoppingapp.presentation.home.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ShoppingRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    init {
        getAllProducts()
    }

    var searchedText by mutableStateOf("")
        private set

    fun onSearchValueChange(value: String) {
        searchedText = value

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

    private fun getAllProducts() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = repository.getAllProducts()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(productList = response.data)
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

    fun errorConsumed() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class SearchScreenUiState(
    val productList: List<ProductEntity> = listOf(),
    val searchResult: List<ProductEntity> = listOf(),
    val errorMessages: List<UiText> = listOf(),
    val isSearchResultEmpty: Boolean = false
)