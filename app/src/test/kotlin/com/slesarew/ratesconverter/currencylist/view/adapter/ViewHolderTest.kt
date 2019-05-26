package com.slesarew.ratesconverter.currencylist.view.adapter

import android.view.View
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import com.slesarew.ratesconverter.databinding.CurrencyItemBinding
import org.junit.Test

class ViewHolderTest {

    private val userItemBindingMock = mock<CurrencyItemBinding> {
        on {
            root
        } doReturn (mock<View>())
    }
    private val onBaseCurrencyCodeChangedMock = mock<OnBaseCurrencyCodeChanged>()
    private val onBaseValueChangedMock = mock<OnBaseValueChanged>()

    private val tested = ViewHolder(
        userItemBindingMock,
        onBaseCurrencyCodeChangedMock,
        onBaseValueChangedMock
    )

    @Test
    fun `binds user`() {
        val model = CurrencyPresentationModel()

        tested.bind(model)

        verify(userItemBindingMock).model = model
    }

    @Test
    fun `binds base currency code changed callback`() {
        val model = CurrencyPresentationModel()

        tested.bind(model)

        verify(userItemBindingMock).onBaseCurrencyCodeChanged = onBaseCurrencyCodeChangedMock
    }

    @Test
    fun `binds base value changed callback`() {
        val model = CurrencyPresentationModel()

        tested.bind(model)

        verify(userItemBindingMock).onBaseValueChanged = onBaseValueChangedMock
    }

    @Test
    fun `executes pending bindings`() {
        tested.bind(CurrencyPresentationModel())

        verify(userItemBindingMock).executePendingBindings()
    }
}