package com.slesarew.ratesconverter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slesarew.ratesconverter.currencylist.view.adapter.CurrencyListAdapter
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel

@BindingAdapter("setData", "scrollToTop")
fun RecyclerView.setData(
    data: List<CurrencyPresentationModel>,
    scrollToTop: Boolean
) {
    if (scrollToTop) smoothScrollToPosition(0)

    (adapter as CurrencyListAdapter).currencies = data
}

@BindingAdapter("scrollToTop")
fun RecyclerView.scrollToTop(scrollToTop: Boolean) {
    if (scrollToTop) smoothScrollToPosition(0)
}

@BindingAdapter("setCurrency")
fun ImageView.setCurrency(symbol: String) =
    resources
        .getIdentifier(
            "currency_${symbol.toLowerCase()}",
            "drawable",
            context.packageName
        )
        .also(this::setImageResource)