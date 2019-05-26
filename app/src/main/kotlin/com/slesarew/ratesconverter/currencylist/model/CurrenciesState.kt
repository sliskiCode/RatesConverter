package com.slesarew.ratesconverter.currencylist.model

import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel

sealed class CurrenciesState {

    data class Data(val currencies: List<CurrencyPresentationModel>) : CurrenciesState()

    object ServerError : CurrenciesState()
}