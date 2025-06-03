package com.example.streamvibe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.streamvibe.databinding.ActivityMainBinding
import com.example.streamvibe.ui.HomeFragment
import com.example.streamvibe.ui.SearchFragment
import com.example.streamvibe.ui.WatchlistFragment
import com.example.streamvibe.ui.ProfileFragment

/**
 * PUBLIC_INTERFACE
 * Main Activity that holds major StreamVibe navigation and UI containers.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)

        // Set default selection and fragment
        showFragment(HomeFragment())
        binding.bottomNav.selectedItemId = R.id.nav_home

        // Handle bottom navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    showFragment(SearchFragment())
                    true
                }
                R.id.nav_watchlist -> {
                    showFragment(WatchlistFragment())
                    true
                }
                R.id.nav_profile -> {
                    showFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    // PUBLIC_INTERFACE
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
