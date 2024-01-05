package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.presentation.navigation.MainDestinations
import com.ahmetocak.shoppingapp.domain.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class PaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val ioDispatcher: CoroutineDispatcher,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Float>(MainDestinations.PAYMENT_AMOUNT_KEY)?.let { amount ->
            _uiState.update {
                it.copy(totalAmount = amount.toDouble())
            }
        }
    }

    var cardNumber by mutableStateOf("")
        private set

    var holderName by mutableStateOf("")
        private set

    var expiryDate by mutableStateOf("")
        private set

    var cvc by mutableStateOf("")
        private set

    fun updateCardNumber(value: String) {
        cardNumber = value
    }

    fun updateHolderName(value: String) {
        holderName = value
    }

    fun updateExpiryDate(value: String) {
        if (value.length <= 4) {
            expiryDate = value
        }
    }

    fun updateCVC(value: String) {
        if (value.length <= 3) {
            cvc = value
        }
    }

    var rotateCard by mutableStateOf(false)
        private set

    fun updateRotateCard(value: Boolean) {
        rotateCard = value
    }

    private fun checkInputFields(): Boolean {
        return !(holderName.isBlank() || cardNumber.isBlank() || expiryDate.isBlank() || cvc.isBlank())
    }

    fun payment() {
        if (checkInputFields()) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update { it.copy(isLoading = true) }
                delay(4000)
                when (val response = shoppingRepository.deleteAllCartItems()) {
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isPaymentDone = true
                            )
                        }
                    }

                    is Response.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessages = listOf(
                                    UiText.StringResource(response.errorMessageId)
                                )
                            )
                        }
                    }
                }
            }
        } else {
            _uiState.update {
                it.copy(errorMessages = listOf(
                    UiText.StringResource(R.string.fill_all_fields)
                ))
            }
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class PaymentUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<UiText> = listOf(),
    val totalAmount: Double = 0.0,
    val isPaymentDone: Boolean = false
)