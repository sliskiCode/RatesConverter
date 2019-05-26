package com.slesarew.ratesconverter.currencylist.viewmodel

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slesarew.ratesconverter.currencylist.model.CurrenciesState
import com.slesarew.ratesconverter.currencylist.model.CurrencyProvider
import com.slesarew.ratesconverter.currencylist.model.PeriodicCurrencyUseCase
import com.slesarew.ratesconverter.currencylist.view.adapter.OnBaseCurrencyCodeChanged
import com.slesarew.ratesconverter.currencylist.view.adapter.OnBaseValueChanged
import com.slesarew.ratesconverter.currencylist.viewmodel.state.ViewState
import com.slesarew.ratesconverter.currencylist.viewmodel.state.ViewStateReducer
import com.slesarew.ratesconverter.currencylist.viewmodel.state.viewStateReducer

fun onBaseCurrencyCodeChanged(currencyProvider: CurrencyProvider) =
    object : OnBaseCurrencyCodeChanged {
        override fun onChanged(currencyCode: String, value: String) {
            with(currencyProvider) {
                changeBaseValue(value)
                changeBaseCurrencyCode(currencyCode)
            }
        }
    }

fun onBaseValueChanged(currencyProvider: CurrencyProvider) =
    object : OnBaseValueChanged {
        override fun onChanged(value: String) {
            currencyProvider.changeBaseValue(value)
        }
    }

class CurrencyListViewModel(
    private val currencyProvider: CurrencyProvider,
    private val stateReducer: ViewStateReducer = viewStateReducer,
    val onBaseCurrencyCodeChanged: OnBaseCurrencyCodeChanged = onBaseCurrencyCodeChanged(currencyProvider),
    val onBaseValueChanged: OnBaseValueChanged = onBaseValueChanged(currencyProvider)
) : ViewModel() {

    private val observer = Observer<CurrenciesState> {
        viewState.applyChanges(stateReducer(viewState, it))
    }

    val viewState: ViewState = ViewState()

    fun start() = currencyProvider.currenciesState.observeForever(observer)

    fun stop() = with(currencyProvider) {
        currenciesState.removeObserver(observer)
        dispose()
    }
}

class CurrencyListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CurrencyListViewModel(PeriodicCurrencyUseCase()) as T
}