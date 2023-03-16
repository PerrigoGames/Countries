package com.perrigogames.countries.ui.countries

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.perrigogames.countries.R
import com.perrigogames.countries.data.Country
import com.perrigogames.countries.data.CountryItemCallback
import com.perrigogames.countries.databinding.FragmentCountriesBinding
import com.perrigogames.countries.databinding.ItemCountryRowBinding

/**
 * The main [Fragment] that fetches a list of countries and displays them in a [RecyclerView].
 */
class CountriesFragment : Fragment() {

    companion object {
        fun newInstance() = CountriesFragment()
    }

    private lateinit var viewModel: CountriesViewModel
    private val adapter = CountryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CountriesViewModel::class.java)

        if (savedInstanceState == null) {
            // Fetch the list of countries async...
            viewModel.fetchCountries(getString(R.string.url_countries_list))
        }

        // ...and listen for the data set to change
        viewModel.countriesList.observe(this) { newList ->
            adapter.submitList(newList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCountriesBinding.inflate(
            /* inflater = */ inflater,
            /* root = */ container,
            /* attachToRoot = */ false,
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerCountries.apply {
            adapter = this@CountriesFragment.adapter
            layoutManager = createLayoutManager()

            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }

        binding.buttonErrorRetry.setOnClickListener {
            viewModel.fetchCountries(getString(R.string.url_countries_list))
        }

        return binding.root
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        val isLandscape = requireContext().resources.displayMetrics.let { dm ->
            dm.widthPixels > dm.heightPixels
        }
        val spanCount = when {
            isLandscape -> 2
            else -> 1
        }
        return GridLayoutManager(
            /* context = */ requireContext(),
            /* spanCount = */ spanCount,
            /* orientation = */ RecyclerView.VERTICAL,
            /* reverseLayout = */ false,
        )
    }

    /**
     * Custom [ListAdapter] class to handle instantiation and binding of [CountryViewHolder]s.
     */
    inner class CountryAdapter : ListAdapter<UiCountry, CountryViewHolder>(CountryItemCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            return CountryViewHolder(
                ItemCountryRowBinding.inflate(
                    /* inflater = */ LayoutInflater.from(requireContext()),
                    /* root = */ parent,
                    /* attachToRoot = */ false,
                )
            )
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    /**
     * [ViewHolder] representing a single row in a list of [Country] objects.
     */
    inner class CountryViewHolder(private val binding: ItemCountryRowBinding) :
        ViewHolder(binding.root) {

        fun bind(country: UiCountry) {
            binding.viewModel = country
        }
    }
}