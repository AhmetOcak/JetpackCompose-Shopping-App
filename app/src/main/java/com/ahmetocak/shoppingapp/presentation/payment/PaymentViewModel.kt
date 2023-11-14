package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

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
}

data class PaymentUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<String> = listOf(),
    val totalAmount: Double = 0.0
)