package com.ahmetocak.shoppingapp.presentation.home.product

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.data.mapper.toProductEntity
import com.ahmetocak.shoppingapp.domain.repository.FirebaseRepository
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.Product
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
class ProductViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenUiState())
    val uiState: StateFlow<ProductScreenUiState> = _uiState.asStateFlow()

    init {
        getCategoryList()
        getAllProducts()
        getFCMTokenAndUpload()
    }

    private fun getCategoryList() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.getCategories()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            categoryUiState = CategoryUiState(
                                isLoading = false,
                                categoryList = response.data.map { category ->
                                    category.replaceFirstChar { char -> char.uppercase() }
                                }
                            )
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            categoryUiState = CategoryUiState(isLoading = false),
                            errorMessages = listOf(
                                UiText.StringResource(resId = response.errorMessageId)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = shoppingRepository.getProducts()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            productUiState = ProductUiState(
                                isLoading = false,
                                productList = response.data
                            )
                        )
                    }
                    saveAllProductsToDb(response.data)
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            productUiState = ProductUiState(isLoading = false),
                            errorMessages = listOf(
                                UiText.StringResource(resId = response.errorMessageId)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun saveAllProductsToDb(productList: List<Product>) {
        viewModelScope.launch(ioDispatcher) {
            productList.forEach {
                shoppingRepository.addProduct(it.toProductEntity())
            }
        }
    }

    private fun getFCMTokenAndUpload() {
        viewModelScope.launch(ioDispatcher) {
            firebaseRepository.getFCMToken().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRepository.uploadUserFCMToken(token = task.result)
                }
            }
        }
    }
}

data class ProductScreenUiState(
    val categoryUiState: CategoryUiState = CategoryUiState(),
    val productUiState: ProductUiState = ProductUiState(),
    val errorMessages: List<UiText> = listOf()
)

data class CategoryUiState(
    val isLoading: Boolean = true,
    val categoryList: List<String> = listOf()
)

data class ProductUiState(
    val isLoading: Boolean = true,
    val productList: List<Product> = listOf()
)