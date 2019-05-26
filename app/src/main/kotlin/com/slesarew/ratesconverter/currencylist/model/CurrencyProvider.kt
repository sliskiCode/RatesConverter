package com.slesarew.ratesconverter.currencylist.model

import androidx.lifecycle.LiveData

interface CurrencyProvider {

    val currenciesState: LiveData<CurrenciesState>

    fun changeBaseCurrencyCode(currencyCode: String)

    fun changeBaseValue(value: String)

    fun dispose()
}