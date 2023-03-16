package com.perrigogames.countries.ui.countries

import com.perrigogames.countries.data.Country

data class UiCountry(
    val countryHeaderText: String,
    val countryCodeText: String,
    val capitalText: String,
)

fun Country.toUiModel() = UiCountry(
    countryHeaderText = "$name, $region",
    countryCodeText = code,
    capitalText = capital,
)