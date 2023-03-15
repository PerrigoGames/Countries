package com.perrigogames.countries.ui.countries

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import coil.decode.SvgDecoder
import coil.load
import com.perrigogames.countries.R
import com.perrigogames.countries.data.Country
import com.perrigogames.countries.data.CountryItemCallback
import com.perrigogames.countries.ui.countries.CountriesViewModel.State.*

/**
 * The main [Fragment] that fetches a list of countries and displays them in a [RecyclerView].
 */
class CountriesFragment : Fragment() {

    companion object {
        fun newInstance() = CountriesFragment()
    }

    /**
     * This would be a [FragmentCountriesBinding] if databinding was working.
     */
    private lateinit var loadingView: ProgressBar
    private lateinit var recyclerCountries: RecyclerView
    private lateinit var textErrorMessage: TextView
    private lateinit var buttonErrorRetry: Button

    private lateinit var viewModel: CountriesViewModel
    private val adapter = CountryAdapter()
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CountriesViewModel::class.java)

        // Fetch the list of countries async...
        viewModel.fetchCountries(getString(R.string.url_countries_list))

        // ...and listen for the data set to change
        viewModel.countriesList.observe(this) { newList ->
            adapter.submitList(newList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutManager = LinearLayoutManager(
            /* context = */ requireContext(),
            /* orientation = */ RecyclerView.VERTICAL,
            /* reverseLayout = */ false,
        )

        val fragmentView = inflater.inflate(R.layout.fragment_countries, container, false)

        recyclerCountries = fragmentView.findViewById(R.id.recycler_countries)
        recyclerCountries.adapter = adapter
        recyclerCountries.layoutManager = layoutManager
        recyclerCountries.addItemDecoration(
            DividerItemDecoration(
                /* context = */ requireContext(),
                /* orientation = */ RecyclerView.VERTICAL,
            )
        )

        loadingView = fragmentView.findViewById(R.id.spinner_loading)
        textErrorMessage = fragmentView.findViewById(R.id.text_error_message)
        buttonErrorRetry = fragmentView.findViewById(R.id.button_error_retry)
        buttonErrorRetry.setOnClickListener {
            viewModel.fetchCountries(getString(R.string.url_countries_list))
        }

        // Listen to a couple fields that depend on the views being found
        viewModel.state.observe(viewLifecycleOwner) { state ->
            loadingView.isVisible = state == FETCHING
            recyclerCountries.isVisible = state == SUCCESS
            textErrorMessage.isVisible = state == ERROR
            buttonErrorRetry.isVisible = state == ERROR
        }

        return fragmentView
    }

    /**
     * Custom [ListAdapter] class to handle instantiation and binding of [CountryViewHolder]s.
     */
    inner class CountryAdapter : ListAdapter<Country, CountryViewHolder>(CountryItemCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_country_row, parent, false)
            return CountryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    /**
     * [ViewHolder] representing a single row in a list of [Country] objects.
     */
    inner class CountryViewHolder(itemView: View) : ViewHolder(itemView) {

        /**
         * DataBinding feature was giving me trouble.  If it had worked, I would pass in the
         * [ItemCountryRowBinding] object and simply store/refer to that.
         */
        private val imageFlag = itemView.findViewById<ImageView>(R.id.image_flag)
        private val textTitleRegion = itemView.findViewById<TextView>(R.id.text_name_region)
        private val textCountryCode = itemView.findViewById<TextView>(R.id.text_country_code)
        private val textCapital = itemView.findViewById<TextView>(R.id.text_capital)

        fun bind(country: Country) {
            Log.v("UI", country.flag)
            if (country.flag.isNotEmpty()) {
                imageFlag.visibility = VISIBLE
                imageFlag.load(country.flag) {
                    decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                }
            } else {
                imageFlag.visibility = INVISIBLE
            }
            textTitleRegion.text = getString(
                R.string.country_name_region_format,
                country.name,
                country.region,
            )
            textCountryCode.text = country.code
            textCapital.text = country.capital
        }
    }
}