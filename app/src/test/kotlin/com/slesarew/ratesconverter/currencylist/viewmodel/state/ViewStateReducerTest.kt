package com.slesarew.ratesconverter.currencylist.viewmodel.state

import com.slesarew.ratesconverter.currencylist.model.CurrenciesState
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ViewStateReducerTest {

    private val viewState = ViewState()

    private val tested = viewStateReducer

    @Test
    fun `produces state with data`() {
        val currencies = listOf(
            CurrencyPresentationModel()
        )
        val newViewState = tested(
            viewState,
            CurrenciesState.Data(
                currencies
            )
        )

        assertEquals(currencies, newViewState.currencies)
    }

    @Test
    fun `produces data state without error`() {
        val currencies = listOf(
            CurrencyPresentationModel()
        )
        val newViewState = tested(
            viewState,
            CurrenciesState.Data(
                currencies
            )
        )

        assertFalse(
            "State has error, but should not!",
            newViewState.hasError
        )
    }

    @Test
    fun `produces data state without loading`() {
        val currencies = listOf(
            CurrencyPresentationModel()
        )
        val newViewState = tested(
            viewState,
            CurrenciesState.Data(
                currencies
            )
        )

        assertFalse(
            "State is loading, but should not!",
            newViewState.loading
        )
    }

    @Test
    fun `produces state that scrolls list to top`() {
        val oldBase = CurrencyPresentationModel(
            "USD",
            "",
            "1.09"
        )
        viewState.currencies = listOf(oldBase)

        val newBase = CurrencyPresentationModel(
            "PLN",
            "Polish Zloty",
            "1"
        )

        val newViewState = tested(
            viewState,
            CurrenciesState.Data(
                listOf(newBase)
            )
        )

        assertTrue(
            "State should scroll to top, but it does not!",
            newViewState.scrollToTop
        )
    }

    @Test
    fun `does not produce state that scrolls list to top`() {
        val base = CurrencyPresentationModel(
            "USD",
            "",
            "1.09"
        )
        viewState.currencies = listOf(base)

        val newViewState = tested(
            viewState,
            CurrenciesState.Data(
                listOf(base)
            )
        )

        assertFalse(
            "State should not scroll to top, but it does!",
            newViewState.scrollToTop
        )
    }

    @Test
    fun `produces error state with empty currencies`() {
        val actual = tested(
            viewState,
            CurrenciesState.ServerError
        )

        assertTrue(
            "Currencies is not empty, but it should!",
            actual.currencies.isEmpty()
        )
    }

    @Test
    fun `produces error state`() {
        val actual = tested(
            viewState,
            CurrenciesState.ServerError
        )

        assertTrue(
            "State does not have an error, but should!",
            actual.hasError
        )
        assertFalse(
            "State is loading, but should not!",
            actual.loading
        )
    }

    @Test
    fun `produces error state without loading`() {
        val actual = tested(
            viewState,
            CurrenciesState.ServerError
        )

        assertFalse(
            "State is loading, but should not!",
            actual.loading
        )
    }
}