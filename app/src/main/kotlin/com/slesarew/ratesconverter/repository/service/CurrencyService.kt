package com.slesarew.ratesconverter.repository.service

import com.slesarew.ratesconverter.repository.model.Currencies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("latest")
    fun getCurrencies(@Query("base") baseSymbol: String): Single<Currencies>

    companion object {

        const val BASE_URL = "https://revolut.duckdns.org/"
    }
}