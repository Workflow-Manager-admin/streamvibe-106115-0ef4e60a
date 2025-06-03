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
        adapter = MediaItemAdapter(viewModel.watchlist) { item ->
            // On click, go to player; long click removes from watchlist
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlayerFragment.newInstance(item.id))
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.setOnLongClickListener {
            val user = AuthRepository.getCurrentUser()
            val pos = (recyclerView.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
            if (user != null && pos != RecyclerView.NO_POSITION) {
                val media = viewModel.watchlist[pos]
                WatchlistRepository.removeFromWatchlist(user, media.id)
                viewModel.refreshWatchlist()
                adapter = MediaItemAdapter(viewModel.watchlist) {/* no-op */ }
                recyclerView.adapter = adapter
            }
            true
        }

        (root as? FrameLayout)?.addView(recyclerView)
        return root
    }
}
