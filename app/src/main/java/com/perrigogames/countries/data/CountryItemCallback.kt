package com.perrigogames.countries.data

import androidx.recyclerview.widget.DiffUtil
import com.perrigogames.countries.ui.countries.UiCountry

/**
 * A [DiffUtil.ItemCallback] extension that checks for equality between [UiCountry] items.
 */
class CountryItemCallback : DiffUtil.ItemCallback<UiCountry>() {
    override fun areItemsTheSame(oldItem: UiCountry, newItem: UiCountry): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: UiCountry, newItem: UiCountry): Boolean {
        return oldItem == newItem
    }
}