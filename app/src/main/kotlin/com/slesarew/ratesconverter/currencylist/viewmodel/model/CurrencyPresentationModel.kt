package com.slesarew.ratesconverter.currencylist.viewmodel.model

private const val EMPTY = ""

data class CurrencyPresentationModel(
    val currencyCode: String = EMPTY,
    val displayName: String = EMPTY,
    val value: String = EMPTY
)