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
        adapter = MediaItemAdapter(viewModel.watchlist) {
            Toast.makeText(context, "Selected: ${it.title}", Toast.LENGTH_SHORT).show()
            // Remove from watchlist on click for mock/demo
            val user = AuthRepository.getCurrentUser()
            if (user != null) {
                WatchlistRepository.removeFromWatchlist(user, it.id)
                viewModel.refreshWatchlist()
                adapter = MediaItemAdapter(viewModel.watchlist) { /* same logic */ }
                recyclerView.adapter = adapter
            }
        }
        recyclerView.adapter = adapter

        (root as? FrameLayout)?.addView(recyclerView)
        return root
    }
}
