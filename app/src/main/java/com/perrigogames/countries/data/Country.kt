package com.perrigogames.countries.data

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val capital: String,
    val code: String,
    val currency: Currency,
    val flag: String,
    val language: Language,
    val name: String,
    val region: String,
)

@Serializable
data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
)

@Serializable
data class Language(
    val code: String,
    val name: String,
)