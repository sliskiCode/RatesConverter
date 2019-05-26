package com.slesarew.ratesconverter.currencylist.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.slesarew.ratesconverter.currencylist.model.CurrencyProvider
import org.junit.Test

class CurrencyListViewModelCallbacksTest {

    private val currencyProviderMock = mock<CurrencyProvider>()

    @Test
    fun `notifies provider about currency code changed`() {
        val tested = onBaseCurrencyCodeChanged(currencyProviderMock)

        tested.onChanged("USD", "12.32")

        verify(currencyProviderMock).changeBaseCurrencyCode("USD")
    }

    @Test
    fun `notifies provider about value changes when base currency code changed`() {
        val tested = onBaseCurrencyCodeChanged(currencyProviderMock)

        tested.onChanged("USD", "12.32")

        verify(currencyProviderMock).changeBaseValue("12.32")
    }

    @Test
    fun `notifies provider about value changed`() {
        val tested = onBaseValueChanged(currencyProviderMock)

        tested.onChanged("12.32")

        verify(currencyProviderMock).changeBaseValue("12.32")
    }

}