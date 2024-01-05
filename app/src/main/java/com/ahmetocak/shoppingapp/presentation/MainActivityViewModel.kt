package com.ahmetocak.shoppingapp.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(consumableViewEvent = listOf(MainUiEvent.Init))
        }
    }

    fun onUiEventConsumed() {
        _uiState.update {
            it.copy(consumableViewEvent = listOf())
        }
    }
}

data class MainUiState(
    val consumableViewEvent: List<MainUiEvent> = listOf()
)

sealed interface MainUiEvent {
    object Init : MainUiEvent
}