package com.example.currencyExchangeApplication.data.api

import com.utilities.Links
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.data.model.Currencies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface CurrencyApiConfig {


    @GET(Links.CUR_LIST_URL)
    suspend fun getSupportedCurrencies(
        @Header("api_key") accessKey: String = Links.API_KEY,
        @Header("format") format: String = "JSON",
    ): List<Currencies>


    @GET(Links.CONVERT_URL)
    suspend fun convertCurrencies(
        @Query("api_key") accessKey: String = Links.API_KEY,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ) : Response<ApiResponse>
}
