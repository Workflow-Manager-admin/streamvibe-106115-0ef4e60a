package com.example.streamvibe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.streamvibe.R

/**
 * PUBLIC_INTERFACE
 * Fragment for user's watchlist.
 */
class WatchlistFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Implement watchlist UI
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }
}
