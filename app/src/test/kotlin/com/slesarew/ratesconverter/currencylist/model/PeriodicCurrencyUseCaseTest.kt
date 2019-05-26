package com.slesarew.ratesconverter.currencylist.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.slesarew.ratesconverter.currencylist.model.CurrenciesState.Data
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import com.slesarew.ratesconverter.repository.CurrencyRepository
import io.reactivex.Flowable
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.concurrent.TimeoutException

class PeriodicCurrencyUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun `emits currency data state`() {
        val currencyRepository = FakeCurrencyRepository()
        val tested = PeriodicCurrencyUseCase(currencyRepository)
        val observer = mock<Observer<CurrenciesState>>()

        tested.currenciesState.observeForever(observer)

        verify(observer).onChanged(
            Data(
                listOf(
                    CurrencyPresentationModel("EUR", "Euro", "1"),
                    CurrencyPresentationModel("PLN", "Polish Zloty", "4.47")
                )
            )
        )
    }

    @Test
    fun `emits server error`() {
        val currencyRepository = ErrorCurrencyRepository()
        val tested = PeriodicCurrencyUseCase(currencyRepository)
        val observer = mock<Observer<CurrenciesState>>()

        tested.currenciesState.observeForever(observer)

        verify(observer).onChanged(
            CurrenciesState.ServerError
        )
    }

    @Test
    fun `remits currency data state when base currency code changed`() {
        val currencyRepository = FakeCurrencyRepository()
        val tested = PeriodicCurrencyUseCase(currencyRepository)
        val observer = mock<Observer<CurrenciesState>>()

        tested.currenciesState.observeForever(observer)

        verify(observer).onChanged(
            Data(
                listOf(
                    CurrencyPresentationModel("EUR", "Euro", "1"),
                    CurrencyPresentationModel("PLN", "Polish Zloty", "4.47")
                )
            )
        )

        tested.changeBaseCurrencyCode("PLN")

        verify(observer).onChanged(
            Data(
                listOf(
                    CurrencyPresentationModel("PLN", "Polish Zloty", "4.47"),
                    CurrencyPresentationModel("EUR", "Euro", "1")
                )
            )
        )
    }

    @Test
    fun `remits recalculated currency data state when base value changed`() {
        val currencyRepository = FakeCurrencyRepository()
        val tested = PeriodicCurrencyUseCase(currencyRepository)
        val observer = mock<Observer<CurrenciesState>>()

        tested.currenciesState.observeForever(observer)

        verify(observer).onChanged(
            Data(
                listOf(
                    CurrencyPresentationModel("EUR", "Euro", "1"),
                    CurrencyPresentationModel("PLN", "Polish Zloty", "4.47")
                )
            )
        )

        tested.changeBaseValue("2")

        verify(observer).onChanged(
            Data(
                listOf(
                    CurrencyPresentationModel("EUR", "Euro", "2"),
                    CurrencyPresentationModel("PLN", "Polish Zloty", "8.94")
                )
            )
        )
    }
}

class FakeCurrencyRepository : CurrencyRepository {

    private val eur = listOf(
        Currency("EUR", "Euro", BigDecimal(1)),
        Currency("PLN", "Polish Zloty", BigDecimal("4.47"))
    )

    private val pln = listOf(
        Currency("PLN", "Polish Zloty", BigDecimal("4.47")),
        Currency("EUR", "Euro", BigDecimal(1))
    )

    override fun getCurrencies(baseSymbol: String): Flowable<List<Currency>> =
        Flowable.just(
            when (baseSymbol) {
                "EUR" -> eur
                "PLN" -> pln
                else -> emptyList()
            }
        )
}

class ErrorCurrencyRepository : CurrencyRepository {

    override fun getCurrencies(baseSymbol: String): Flowable<List<Currency>> =
        Flowable.error(TimeoutException())
}