package com.slesarew.ratesconverter.repository.model

data class Currencies(
    val base: String = "",
    val date: String = "",
    val rates: Map<String, String> = emptyMap()
)