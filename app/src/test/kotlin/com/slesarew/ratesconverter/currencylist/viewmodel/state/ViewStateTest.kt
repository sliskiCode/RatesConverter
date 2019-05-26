package com.slesarew.ratesconverter.currencylist.viewmodel.state

import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import org.junit.Test
import kotlin.test.assertEquals

class ViewStateTest {

    @Test
    fun `applies changes to view state`() {
        val reducedViewState = ViewState().apply {
            currencies = listOf(
                CurrencyPresentationModel(),
                CurrencyPresentationModel()
            )
            hasError = true
            loading = false
            scrollToTop = false
        }

        val viewState = ViewState().apply {
            applyChanges(reducedViewState)
        }

        assertEquals(reducedViewState.currencies, viewState.currencies)
        assertEquals(reducedViewState.hasError, viewState.hasError)
        assertEquals(reducedViewState.loading, viewState.loading)
        assertEquals(reducedViewState.scrollToTop, viewState.scrollToTop)
    }
}