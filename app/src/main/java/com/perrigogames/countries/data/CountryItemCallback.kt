package com.perrigogames.countries.data

import androidx.recyclerview.widget.DiffUtil

/**
 * A [DiffUtil.ItemCallback] extension that checks for equality between [Country] items.
 */
class CountryItemCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}