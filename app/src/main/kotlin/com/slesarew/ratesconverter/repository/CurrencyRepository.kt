package com.slesarew.ratesconverter.repository

import com.slesarew.ratesconverter.currencylist.model.Currency
import io.reactivex.Flowable

interface CurrencyRepository {

    fun getCurrencies(baseSymbol: String): Flowable<List<Currency>>
}