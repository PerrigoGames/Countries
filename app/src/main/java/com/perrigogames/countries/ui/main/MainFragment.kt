package com.perrigogames.countries.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.perrigogames.countries.R
import com.perrigogames.countries.data.Country
import com.perrigogames.countries.data.CountryItemCallback

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val adapter = CountryAdapter()
    private val layoutManager = LinearLayoutManager(
        /* context = */ requireContext(),
        /* orientation = */ RecyclerView.VERTICAL,
        /* reverseLayout = */ false,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
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
        private val textTitleRegion = itemView.findViewById<TextView>(R.id.text_name_region)
        private val textCountryCode = itemView.findViewById<TextView>(R.id.text_country_code)
        private val textCapital = itemView.findViewById<TextView>(R.id.text_capital)

        fun bind(country: Country) {
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