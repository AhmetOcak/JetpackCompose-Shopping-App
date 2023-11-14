package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmetocak.shoppingapp.utils.NavKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Float>(NavKeys.TOTAL_AMOUNT)?.let { amount ->
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
}

data class PaymentUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<String> = listOf(),
    val totalAmount: Double = 0.0
)