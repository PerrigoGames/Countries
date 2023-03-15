package com.perrigogames.countries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.perrigogames.countries.ui.countries.CountriesFragment

class CountriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CountriesFragment.newInstance())
                .commitNow()
        }
    }
}