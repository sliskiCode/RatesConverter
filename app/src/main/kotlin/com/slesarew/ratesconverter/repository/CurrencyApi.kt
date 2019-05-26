package com.slesarew.ratesconverter.repository

import com.slesarew.ratesconverter.currencylist.model.Currency
import com.slesarew.ratesconverter.repository.model.Currencies
import com.slesarew.ratesconverter.repository.service.CurrencyService
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

val currencyService: CurrencyService by lazy {
    Retrofit.Builder()
        .baseUrl(CurrencyService.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyService::class.java)
}

class CurrencyApi(
    private val service: CurrencyService = currencyService,
    private val backgroundScheduler: Scheduler = Schedulers.io()
) : CurrencyRepository {

    override fun getCurrencies(baseSymbol: String): Flowable<List<Currency>> = service
        .getCurrencies(baseSymbol)
        .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
        .map(this::mapCurrenciesToCurrencyList)
        .subscribeOn(backgroundScheduler)

    private fun mapCurrenciesToCurrencyList(currencies: Currencies): List<Currency> =
        mutableListOf(
            toCurrency(toCurrencyInfo(currencies.base))
        ).plus(
            currencies.rates
                .map { rate ->
                    toCurrency(toCurrencyInfo(rate.key), rate.value)
                }
        )

    private fun toCurrency(currencyInfo: Pair<String, String>, value: String = "1") =
        currencyInfo.let { (currencyCode, displayName) ->
            Currency(
                currencyCode,
                displayName,
                BigDecimal(value)
            )
        }

    private fun toCurrencyInfo(symbol: String): Pair<String, String> =
        java.util.Currency
            .getInstance(symbol)
            .let {
                it.currencyCode to it.displayName
            }
}