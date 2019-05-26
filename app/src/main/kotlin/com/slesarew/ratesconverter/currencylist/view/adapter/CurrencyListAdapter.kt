package com.slesarew.ratesconverter.currencylist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.slesarew.ratesconverter.currencylist.view.adapter.ViewType.BASE
import com.slesarew.ratesconverter.currencylist.view.adapter.ViewType.OTHER
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import com.slesarew.ratesconverter.databinding.CurrencyItemBinding

typealias DataSetChangedNotifier = (Adapter<ViewHolder>) -> Unit

class CurrencyListAdapter(
    private val onBaseCurrencyCodeChanged: OnBaseCurrencyCodeChanged,
    private val onBaseValueChanged: OnBaseValueChanged,
    private val dataSetChangedNotifier: DataSetChangedNotifier = Adapter<ViewHolder>::notifyDataSetChanged
) : Adapter<ViewHolder>() {

    var currencies: List<CurrencyPresentationModel> = emptyList()
        set(value) {
            field = value

            dataSetChangedNotifier.invoke(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CurrencyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onBaseCurrencyCodeChanged,
            getOnBaseValueChanged(viewType)
        )

    private fun getOnBaseValueChanged(viewType: Int) =
        when (viewType) {
            BASE -> onBaseValueChanged
            else -> OnBaseValueChanged.NoOp()
        }

    override fun getItemCount(): Int =
        currencies.size

    override fun getItemId(position: Int): Long =
        currencies[position]
            .displayName
            .hashCode()
            .toLong()

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> BASE
            else -> OTHER
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(currencies[position])
}

object ViewType {

    const val BASE = 0
    const val OTHER = 1
}

open class ViewHolder(
    private val userItemBinding: CurrencyItemBinding,
    private val onBaseCurrencyCodeChanged: OnBaseCurrencyCodeChanged?,
    private val onBaseValueChanged: OnBaseValueChanged?
) : RecyclerView.ViewHolder(userItemBinding.root) {

    open fun bind(currencyPresentationModel: CurrencyPresentationModel) {
        userItemBinding.model = currencyPresentationModel
        userItemBinding.onBaseCurrencyCodeChanged = onBaseCurrencyCodeChanged
        userItemBinding.onBaseValueChanged = onBaseValueChanged
        userItemBinding.executePendingBindings()
    }
}

interface OnBaseCurrencyCodeChanged {

    fun onChanged(currencyCode: String, value: String)
}

interface OnBaseValueChanged {

    fun onChanged(value: String)

    class NoOp : OnBaseValueChanged {

        override fun onChanged(value: String) = Unit
    }
}