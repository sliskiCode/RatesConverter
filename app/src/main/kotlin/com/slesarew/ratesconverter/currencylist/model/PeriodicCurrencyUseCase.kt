package com.slesarew.ratesconverter.currencylist.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.slesarew.ratesconverter.currencylist.model.CurrenciesState.Data
import com.slesarew.ratesconverter.currencylist.model.CurrenciesState.ServerError
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import com.slesarew.ratesconverter.repository.CurrencyApi
import com.slesarew.ratesconverter.repository.CurrencyRepository
import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.RoundingMode.CEILING

private const val DEFAULT_BASE_CURRENCY_CODE = "EUR"
private const val ROUND_SCALE = 2

class PeriodicCurrencyUseCase(
    private val currencyRepository: CurrencyRepository = CurrencyApi()
) : CurrencyProvider {

    private val state = MutableLiveData<CurrenciesState>()

    private var currencyToValues = emptyList<Currency>()

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val baseState = BehaviorSubject.createDefault<String>(
        DEFAULT_BASE_CURRENCY_CODE
    )

    private val baseValue = BehaviorSubject.create<BigDecimal>()

    override val currenciesState: LiveData<CurrenciesState>
        get() {
            if (disposable.size() == 0) {
                disposable.addAll(
                    currenciesStateStream(),
                    baseValueStream()
                )
            }

            return state
        }

    private fun currenciesStateStream() = baseState
        .toFlowable(LATEST)
        .switchMap(::toCurrenciesStream)
        .subscribe(state::postValue)

    private fun toCurrenciesStream(baseSymbol: String) = baseState
        .let {
            currencyRepository
                .getCurrencies(baseSymbol)
                .doOnNext { currencyToValues = it }
                .map {
                    if (baseValue.hasValue()) {
                        toCurrenciesState(currencyToValues, baseValue.value)
                    } else {
                        toCurrenciesState(currencyToValues)
                    }
                }
                .onErrorReturnItem(ServerError)
        }

    private fun baseValueStream() = baseValue
        .subscribe {
            state.postValue(
                toCurrenciesState(currencyToValues, it)
            )
        }

    private fun toCurrenciesState(
        currencyToValues: List<Currency>,
        value: BigDecimal = BigDecimal(1)
    ) = currencyToValues
        .map {
            CurrencyPresentationModel(
                it.currencyCode,
                it.displayName,
                it.value
                    .multiply(value)
                    .setScale(ROUND_SCALE, CEILING)
                    .stripTrailingZeros()
                    .toPlainString()
            )
        }
        .toCurrenciesDataState()

    private fun List<CurrencyPresentationModel>.toCurrenciesDataState(): CurrenciesState = Data(this)

    override fun changeBaseCurrencyCode(currencyCode: String) = baseState.onNext(currencyCode)

    override fun changeBaseValue(value: String) = baseValue.onNext(toValue(value))

    private fun toValue(value: String) = if (value.isEmpty()) BigDecimal(0) else value.toBigDecimal()

    override fun dispose() = disposable.clear()
}