package com.perrigogames.countries.ui.countries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.perrigogames.countries.data.Country
import com.perrigogames.countries.net.CountriesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The [ViewModel] for [CountriesFragment] that handles fetching and processing the remote list
 * of countries for the UI.
 */
class CountriesViewModel : ViewModel() {

    /**
     * An enum class describing the current state of the ViewModel's fetching operation.
     */
    enum class State {
        FETCHING, // The fetch operation is ongoing, show a loading spinner.
        SUCCESS, // The fetch is successful, show the resulting list of countries.
        ERROR, // The fetch failed, show an error message.
    }

    val countriesList = MutableLiveData<List<UiCountry>>()
    private val state = MutableLiveData<State>()

    val loadingVisible = state.map { it == State.FETCHING }
    val countriesVisible = state.map { it == State.SUCCESS }
    val errorVisible = state.map { it == State.ERROR }

    private var lastFetchUrl: String? = null

    /**
     * Asynchronously fetches a list of [Country] objects, updating the [state] and [countriesList]
     * LiveData objects as needed.
     * @param url the URL to GET the list of countries from
     */
    fun fetchCountries(url: String) {
        lastFetchUrl = url
        countriesList.value = emptyList()
        state.value = State.FETCHING

        // Launch a computation coroutine to prevent blocking the main thread.
        viewModelScope.launch(Dispatchers.IO) {
            val response = CountriesApi.getCountries(url)
            if (response.isSuccess()) {
                viewModelScope.launch(Dispatchers.Main) {
                    Log.v("Request", "Setting countries: ${response.countries!!.size} entries")
                    state.value = State.SUCCESS
                    countriesList.value = response.countries.map { it.toUiModel() }
                }
            } else {
                response.exception!!.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) {
                    state.value = State.ERROR
                }
            }
        }
    }

    /**
     * Refetches the list of countries using the same URL as the last time [fetchCountries]
     * was called.
     * @throws Error if [fetchCountries] has not been previously called.
     */
    fun refetchCountries() {
        lastFetchUrl?.let {
            fetchCountries(it)
        } ?: error("Must call `fetchCountries` first to set the URL")
    }
}