package com.slesarew.ratesconverter.currencylist.model

import java.math.BigDecimal

data class Currency(
    val currencyCode: String,
    val displayName: String,
    val value: BigDecimal
)