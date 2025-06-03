package com.example.streamvibe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.streamvibe.R

/**
 * PUBLIC_INTERFACE
 * Fragment for searching content.
 */
class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Implement search UI
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}
