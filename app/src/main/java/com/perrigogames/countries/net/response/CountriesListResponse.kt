package com.perrigogames.countries.net.response

import com.perrigogames.countries.data.Country

/**
 * Response object for [CountriesApi.getCountries].
 * @param countries The list of [Country] objects received from the backend.  Guaranteed to be null
 * if [exception] exists.
 * @param exception The error encountered while processing the API call.  Guaranteed to be null
 * if [countries] exists.
 */
class CountriesListResponse private constructor(
    val countries: List<Country>? = null,
    val exception: Exception? = null,
) {

    constructor(countries: List<Country>) : this(countries = countries, exception = null)

    constructor(exception: Exception) : this(countries = null, exception = exception)

    fun isSuccess() = exception == null
}
