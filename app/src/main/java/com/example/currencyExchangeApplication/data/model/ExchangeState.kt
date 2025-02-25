package com.example.currencyExchangeApplication.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeState(
    val cur1: String = "EUR",
    val cur2: String = "USD",
    val num1: String = "",
    val num2: String = "",
    val isLoading: Boolean = false
) : Parcelable