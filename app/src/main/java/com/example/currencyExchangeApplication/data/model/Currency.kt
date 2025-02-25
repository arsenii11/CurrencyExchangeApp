package com.example.currencyExchangeApplication.data.model



import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("base_currency_code")
    val baseCurrencyCode: String,
    @SerializedName("base_currency_name")
    val baseCurrencyName: String,
    val amount: String,
    @SerializedName("updated_date")
    val updatedDate: String,
    val rates: Map<String, Rates>,
    val status: String
)

data class Rates(
    @SerializedName("currency_name")
    val currencyName: String,
    val rate: String,
    @SerializedName("rate_for_amount")
    val rateForAmount: String
)