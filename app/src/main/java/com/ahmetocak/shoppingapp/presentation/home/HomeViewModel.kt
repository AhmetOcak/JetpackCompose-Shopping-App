package com.ahmetocak.shoppingapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Error
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.data.repository.shopping.ShoppingRepository
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
                            errorMessages = _uiState.value.errorMessages + listOf(response.error)
                        )
                    }
                }
            }
        }
    }
}

data class HomeScreeUiState(
    val isCategoryListLoading: Boolean = true,
    val errorMessages: List<Error> = listOf(),
    val categoryList: List<String> = listOf()
)