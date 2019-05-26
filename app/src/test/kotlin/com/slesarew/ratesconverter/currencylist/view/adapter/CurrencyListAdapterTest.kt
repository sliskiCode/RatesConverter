package com.slesarew.ratesconverter.currencylist.view.adapter

import android.view.View
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyListAdapterTest {

    private val onBaseCurrencyCodeChangedMock = mock<OnBaseCurrencyCodeChanged>()
    private val onBaseValueChangedMock = mock<OnBaseValueChanged>()
    private val notifyDataSetChangedMock = mock<DataSetChangedNotifier>()

    private val tested = CurrencyListAdapter(
        onBaseCurrencyCodeChangedMock,
        onBaseValueChangedMock,
        notifyDataSetChangedMock
    )

    @Test
    fun `returns correct number of users`() {
        tested.currencies = listOf(
            CurrencyPresentationModel(),
            CurrencyPresentationModel(),
            CurrencyPresentationModel(),
            CurrencyPresentationModel()
        )

        val actual = tested.itemCount

        assertEquals(4, actual)
    }

    @Test
    fun `notifies about data set changed`() {
        val currencies = listOf(CurrencyPresentationModel())

        tested.currencies = currencies

        verify(notifyDataSetChangedMock).invoke(tested)
    }

    @Test
    fun `binds view holder with correct data`() {
        val currencyPresentationModel = CurrencyPresentationModel()
        tested.currencies = listOf(currencyPresentationModel)
        val viewHolder = ViewHolderSpy()

        tested.onBindViewHolder(viewHolder, 0)

        assertEquals(currencyPresentationModel, viewHolder.currencyPresentationModel)
    }

    @Test
    fun `returns base view type for first position`() {
        val viewType = tested.getItemViewType(0)

        assertEquals(ViewType.BASE, viewType)
    }

    @Test
    fun `returns other view type for position different than first`() {
        val viewType = tested.getItemViewType(1)

        assertEquals(ViewType.OTHER, viewType)
    }

    @Test
    fun `return correct item id`() {
        val displayName = "USA Dollar"
        tested.currencies = listOf(
            CurrencyPresentationModel(
                "USD",
                displayName,
                "1.23"
            )
        )

        val itemId = tested.getItemId(0)

        assertEquals(displayName.hashCode().toLong(), itemId)
    }
}

class ViewHolderSpy : ViewHolder(
    mock {
        on {
            root
        } doReturn (mock<View>())
    },
    mock(),
    mock()
) {

    var currencyPresentationModel: CurrencyPresentationModel? = null

    override fun bind(currencyPresentationModel: CurrencyPresentationModel) {
        this.currencyPresentationModel = currencyPresentationModel
    }
}