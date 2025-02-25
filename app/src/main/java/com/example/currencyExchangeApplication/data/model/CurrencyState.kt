package com.example.currencyExchangeApplication.data.model

import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.flow.MutableSharedFlow



// Encapsulates screen state and actions
data class ExchangeScreenState(
    val currency1: CurrencyState = CurrencyState(),
    val currency2: CurrencyState = CurrencyState(),
    val isLoading: Boolean = false,
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),
    val errorMessage: MutableSharedFlow<String> = MutableSharedFlow()
)

data class CurrencyState(
    val selectedCurrency: String = "USD",
    val amount: String = "",
    val onCurrencyChange: (String) -> Unit = {},
    val onAmountChange: (String) -> Unit = {}
)
