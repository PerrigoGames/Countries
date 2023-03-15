package com.perrigogames.countries.ui.countries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perrigogames.countries.data.Country
import com.perrigogames.countries.net.ktorHttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Thread.sleep

/**
 * The [ViewModel]
 */
class CountriesViewModel : ViewModel() {

    enum class State {
        FETCHING, SUCCESS, ERROR
    }

    val countriesList = MutableLiveData<List<Country>>()
    val state = MutableLiveData<State>()

    fun fetchCountries(url: String) {
        countriesList.value = emptyList()
        state.value = State.FETCHING

        // Launch a computation coroutine to prevent blocking the main thread.
        viewModelScope.launch(Dispatchers.IO) {
            val response = ktorHttpClient.get(url)
            sleep(1000) // Simulate longer operation to show loading spinner

            if (response.status.isSuccess()) {
                try {
                    // This works around the mismatched content type from the server.
                    val countries = Json.decodeFromString<List<Country>>(response.bodyAsText())

                    // Once computation is done, set the LiveData on the main thread.
                    viewModelScope.launch(Dispatchers.Main) {
                        Log.v("Request", "Setting countries: ${countries.size} entries")
                        state.value = State.SUCCESS
                        countriesList.value = countries
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    viewModelScope.launch(Dispatchers.Main) {
                        state.value = State.ERROR
                    }
                }
            } else {
                Log.w("Request", response.status.toString())
                viewModelScope.launch(Dispatchers.Main) {
                    state.value = State.ERROR
                }
            }
        }
    }
}