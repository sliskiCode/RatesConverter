package com.slesarew.ratesconverter.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.slesarew.ratesconverter.currencylist.model.Currency
import com.slesarew.ratesconverter.repository.model.Currencies
import com.slesarew.ratesconverter.repository.service.CurrencyService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.math.BigDecimal

private const val BASE = "USD"

class CurrencyApiTest {

    private val currencies = Currencies(
        base = BASE,
        rates = mapOf(
            "PLN" to "10",
            "AUD" to "1",
            "CZK" to "4",
            "HRK" to "8"
        )
    )

    private val serviceMock = mock<CurrencyService> {
        on { getCurrencies(BASE) } doReturn Single.just(currencies)
    }

    private val tested = CurrencyApi(serviceMock, Schedulers.trampoline())

    @Test
    fun `return users stream`() {
        val test = tested.getCurrencies(BASE).test()

        test.assertValue(
            listOf(
                Currency("USD", "US Dollar", BigDecimal(1)),
                Currency("PLN", "Polish Zloty", BigDecimal(10)),
                Currency("AUD", "Australian Dollar", BigDecimal(1)),
                Currency("CZK", "Czech Republic Koruna", BigDecimal(4)),
                Currency("HRK", "Kuna", BigDecimal(8))
            )
        )
    }
}