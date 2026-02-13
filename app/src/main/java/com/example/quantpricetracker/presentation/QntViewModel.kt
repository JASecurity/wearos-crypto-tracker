package com.example.quantpricetracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class QntUiState(
    val price: Double? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val lastUpdated: String = ""
)

class QntViewModel : ViewModel() {
    private val api = CoinGeckoApi.create()

    private val _uiState = MutableStateFlow(QntUiState())
    val uiState: StateFlow<QntUiState> = _uiState

    init {
        fetchPrice()
        startAutoRefresh()
    }

    fun fetchPrice() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = api.getPrice()
                val price = response.`quant-network`.cad
                val currentTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                    .format(java.util.Date())

                _uiState.value = QntUiState(
                    price = price,
                    isLoading = false,
                    error = null,
                    lastUpdated = currentTime
                )
            } catch (e: Exception) {
                _uiState.value = QntUiState(
                    price = null,
                    isLoading = false,
                    error = "Failed to fetch price",
                    lastUpdated = ""
                )
            }
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(60000) // Refresh every 60 seconds
                fetchPrice()
            }
        }
    }
}
