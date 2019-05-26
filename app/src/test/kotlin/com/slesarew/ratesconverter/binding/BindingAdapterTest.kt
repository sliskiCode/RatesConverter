package com.slesarew.ratesconverter.binding

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.slesarew.ratesconverter.R
import com.slesarew.ratesconverter.currencylist.view.adapter.CurrencyListAdapter
import com.slesarew.ratesconverter.currencylist.viewmodel.model.CurrencyPresentationModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class BindingAdapterTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun `sets adapter data`() = with(RecyclerView(context)) {
        adapter = CurrencyListAdapter(mock(), mock())
        val users = listOf(CurrencyPresentationModel())

        setData(users, false)

        assertEquals(users, (adapter as CurrencyListAdapter).currencies)
    }

    @Test
    fun `scrolls to top`() = with(spy(RecyclerView(context))) {
        scrollToTop(true)

        verify(this).smoothScrollToPosition(0)
    }

    @Test
    fun `does not scroll to top`() = with(spy(RecyclerView(context))) {
        scrollToTop(false)

        verify(this, never()).smoothScrollToPosition(0)
    }

    @Test
    fun `sets usd currency flag`() = with(ImageView(context)) {
        setCurrency("USD")

        hasImage(R.drawable.currency_usd)
    }

    @Test
    fun `sets pln currency flag`() = with(ImageView(context)) {
        setCurrency("PLN")

        hasImage(R.drawable.currency_pln)
    }

    private fun ImageView.hasImage(@DrawableRes resId: Int) {
        val drawableResId = Shadows.shadowOf(drawable).createdFromResId

        Assert.assertEquals(resId, drawableResId)
    }
}