package com.slesarew.ratesconverter.currencylist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.slesarew.ratesconverter.currencylist.view.adapter.CurrencyListAdapter
import com.slesarew.ratesconverter.currencylist.viewmodel.CurrencyListViewModel
import com.slesarew.ratesconverter.currencylist.viewmodel.CurrencyListViewModelFactory
import com.slesarew.ratesconverter.databinding.FragmentCurrencyListBinding
import kotlin.LazyThreadSafetyMode.NONE

class CurrencyListFragment : Fragment() {

    private val viewModel by lazy(NONE) {
        ViewModelProviders
            .of(this@CurrencyListFragment, CurrencyListViewModelFactory())
            .get<CurrencyListViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCurrencyListBinding
        .inflate(inflater, container, false)
        .apply {
            state = viewModel.viewState

            with(list) {
                adapter = CurrencyListAdapter(
                    viewModel.onBaseCurrencyCodeChanged,
                    viewModel.onBaseValueChanged
                ).apply {
                    setHasStableIds(true)
                }
            }
        }
        .root

    override fun onStart() {
        super.onStart()

        viewModel.start()
    }

    override fun onStop() {
        super.onStop()

        viewModel.stop()
    }
}