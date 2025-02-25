package com.example.currencyExchangeApplication.data.api

import com.utilities.Links
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Links.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CurrencyApiConfig by lazy {
        retrofit.create(CurrencyApiConfig::class.java)
    }
}