package com.perrigogames.countries.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.perrigogames.countries.R
import com.perrigogames.countries.data.Country

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

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
     * [ViewHolder] representing a single row in a list of [Country] objects.
     */
    inner class CountryViewHolder(itemView: View) : ViewHolder(itemView) {

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