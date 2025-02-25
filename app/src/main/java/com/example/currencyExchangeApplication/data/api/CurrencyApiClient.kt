package com.example.currencyExchangeApplication.data.api

import com.example.currencyExchangeApplication.data.model.ApiResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


/*
interface CurrencyApiClient {
    fun getCurrencyExchange(
        accessKey: String,
        from: String,
        to: String,
        amount: Double
    ): Response<ApiResponse>
}

@Singleton
class CurrencyApiManager @Inject constructor(
    private val apiConfig: CurrencyApiConfig,
) : CurrencyApiClient {
    override fun getCurrencyExchange(
        accessKey: String,
        from: String,
        to: String,
        amount: Double
    ): Response<ApiResponse> {
        return RetrofitInstance.api.convertCurrencies(accessKey, from, to, amount)
    }

}*/
