package com.perrigogames.countries.net

import com.perrigogames.countries.data.Country
import com.perrigogames.countries.net.response.CountriesListResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * API class for managing the possible endpoints for the Countries service.
 * (Ideally this would be a class with an injected HTTP client, but this was skipped for simplicity)
 */
object CountriesApi {

    /**
     * Requests a list of countries from the backend.
     * @param url The URL to request (this would ideally be dependency injected)
     */
    suspend fun getCountries(url: String): CountriesListResponse {
        val response = ktorHttpClient.get(url)
        Thread.sleep(1000) // Simulate longer operation to show loading spinner
        return if (response.status.isSuccess()) {
            try {
                // This works around the mismatched content type from the server.
                val countries = Json.decodeFromString<List<Country>>(response.bodyAsText())
                CountriesListResponse(countries)
            } catch (e: Exception) {
                CountriesListResponse(e)
            }
        } else {
            CountriesListResponse(Exception(response.status.toString()))
        }
    }
}
