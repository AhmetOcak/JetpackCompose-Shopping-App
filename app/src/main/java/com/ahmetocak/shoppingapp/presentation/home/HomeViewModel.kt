package com.ahmetocak.shoppingapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.data.mapper.toProductEntity
import com.ahmetocak.shoppingapp.domain.repository.FirebaseRepository
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val firebaseRepository: FirebaseRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreeUiState())
    val uiState: StateFlow<HomeScreeUiState> = _uiState.asStateFlow()

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
                            isCategoryListLoading = false,
                            categoryList = response.data
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            isCategoryListLoading = false,
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
                            isProductListLoading = false,
                            productList = response.data
                        )
                    }
                    saveAllProductsToDb(response.data)
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(
                            isProductListLoading = false,
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
                    firebaseAuth.uid?.let {
                        firebaseRepository.uploadUserFCMToken(
                            token = task.result,
                            userUid = it
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
    val errorMessages: List<UiText> = listOf()
)