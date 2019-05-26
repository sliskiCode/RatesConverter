package com.slesarew.ratesconverter.currencylist.viewmodel.state

import com.slesarew.ratesconverter.currencylist.model.CurrenciesState

typealias ViewStateReducer = (old: ViewState, currenciesState: CurrenciesState) -> ViewState

val viewStateReducer: ViewStateReducer = { old, currenciesState ->
    ViewState()
        .apply {
            when (currenciesState) {
                is CurrenciesState.Data -> {
                    old.currencies
                        .firstOrNull()
                        ?.let {
                            val newBase = currenciesState.currencies.first().currencyCode
                            scrollToTop = it.currencyCode != newBase
                        }
                    currencies = currenciesState.currencies
                    hasError = false
                    loading = false
                }
                is CurrenciesState.ServerError -> {
                    scrollToTop = false
                    currencies = emptyList()
                    hasError = true
                    loading = false
                }
            }
        }
}