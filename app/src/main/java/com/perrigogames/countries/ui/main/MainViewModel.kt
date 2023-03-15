package com.perrigogames.countries.ui.main

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

class MainViewModel : ViewModel() {

    val countriesList = MutableLiveData<List<Country>>()
    val countriesFetchError = MutableLiveData(false)

    fun fetchCountries(url: String) {
        countriesFetchError.value = false

        viewModelScope.launch(Dispatchers.IO) {
            val response = ktorHttpClient.get(url)
            if (response.status.isSuccess()) {
                val countries = Json.decodeFromString<List<Country>>(response.bodyAsText())
                viewModelScope.launch(Dispatchers.Main) {
                    Log.v("Request", "Setting countries: ${countries.size} entries")
                    countriesList.value = countries
                }
            } else {
                countriesFetchError.value = true
            }
        }
    }
}