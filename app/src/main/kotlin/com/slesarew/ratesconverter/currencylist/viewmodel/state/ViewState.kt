package com.slesarew.ratesconverter.currencylist.viewmodel.state

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.slesarew.ratesconverter.BR
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel

class ViewState : BaseObservable() {

    @Bindable
    var loading = true
        set(value) {
            if (field != value) {
                field = value

                notifyPropertyChanged(BR.loading)
            }
        }

    @Bindable
    var currencies = emptyList<CurrencyPresentationModel>()
        set(value) {
            if (field != value) {
                field = value

                notifyPropertyChanged(BR.currencies)
            }
        }

    @Bindable
    var hasError = false
        set(value) {
            if (field != value) {
                field = value

                notifyPropertyChanged(BR.hasError)
            }
        }

    @Bindable
    var scrollToTop = true
        set(value) {
            if (field != value) {
                field = value

                notifyPropertyChanged(BR.scrollToTop)
            }
        }

    fun applyChanges(viewState: ViewState) {
        loading = viewState.loading
        currencies = viewState.currencies
        hasError = viewState.hasError
        scrollToTop = viewState.scrollToTop
    }
}