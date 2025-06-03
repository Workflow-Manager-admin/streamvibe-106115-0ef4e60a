package com.example.streamvibe.ui

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamvibe.R
import com.example.streamvibe.model.MediaItem
import com.example.streamvibe.repository.AuthRepository
import com.example.streamvibe.repository.ContentRepository
import com.example.streamvibe.repository.WatchlistRepository
import com.example.streamvibe.ui.adapter.MediaItemAdapter

// PUBLIC_INTERFACE
class WatchlistViewModel : ViewModel() {
    var watchlist: List<MediaItem> = emptyList()

    fun refreshWatchlist() {
        val user = AuthRepository.getCurrentUser()
        watchlist = if (user != null) {
            WatchlistRepository.getUserWatchlist(user)
                .mapNotNull { ContentRepository.getMediaById(it) }
        } else {
            emptyList()
        }
    }
}

// PUBLIC_INTERFACE
class WatchlistFragment : Fragment() {
    private lateinit var viewModel: WatchlistViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MediaItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_watchlist, container, false)
        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]
        viewModel.refreshWatchlist()

        recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MediaItemAdapter(
            viewModel.watchlist,
            onItemClick = { item ->
                // On click, go to player screen
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PlayerFragment.newInstance(item.id))
                    .addToBackStack(null)
                    .commit()
            },
            showWatchlistButton = true,
            onWatchlistToggle = { item, add ->
                val user = AuthRepository.getCurrentUser()
                if (user != null) {
                    if (!add) {
                        WatchlistRepository.removeFromWatchlist(user, item.id)
                        Toast.makeText(requireContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show()
                    }
                    // Do not allow add in WatchlistFragment (since listed means already in)
                    // Refresh UI
                    viewModel.refreshWatchlist()
                    recyclerView.adapter = MediaItemAdapter(
                        viewModel.watchlist,
                        onItemClick = { /* handle as above */ },
                        showWatchlistButton = true,
                        onWatchlistToggle = this::onWatchlistToggleInternal // helper to avoid code duplication
                    )
                }
            }
        )
        recyclerView.adapter = adapter

        (root as? FrameLayout)?.addView(recyclerView)
        return root
    }

    // Helper for onWatchlistToggle callback reuse
    private fun onWatchlistToggleInternal(item: MediaItem, add: Boolean) {
        val user = AuthRepository.getCurrentUser()
        if (user != null && !add) {
            WatchlistRepository.removeFromWatchlist(user, item.id)
            Toast.makeText(requireContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show()
            viewModel.refreshWatchlist()
            recyclerView.adapter = MediaItemAdapter(
                viewModel.watchlist,
                onItemClick = { /* no-op or show player */ },
                showWatchlistButton = true,
                onWatchlistToggle = this::onWatchlistToggleInternal
            )
        }
    }
}
