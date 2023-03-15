package com.perrigogames.countries.net

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * A Ktor [HttpClient] capable of making requests and handling JSON bodies in requests and
 * responses.
 */
internal val ktorHttpClient = HttpClient(Android) {

    install(ContentNegotiation) {
        /**
         * This wasn't auto-deserializing properly due to the content type being `text/plain`, which
         * isn't automatically caught by [ContentNegotiation].  It's worked around directly in
         * [CountriesViewModel].
         */
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}