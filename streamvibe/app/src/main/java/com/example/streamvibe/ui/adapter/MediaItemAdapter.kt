package com.example.streamvibe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamvibe.R
import com.example.streamvibe.model.MediaItem

/**
 * PUBLIC_INTERFACE
 * Adapter for displaying a list of MediaItem(s) as cards.
 * Used in Home, Search, Watchlist, and Recommendations screens.
 */
import android.widget.Button
import com.example.streamvibe.repository.AuthRepository
import com.example.streamvibe.repository.WatchlistRepository

class MediaItemAdapter(
    private val items: List<MediaItem>,
    private val onItemClick: ((MediaItem) -> Unit)? = null,
    private val showWatchlistButton: Boolean = false,
    private val onWatchlistToggle: ((MediaItem, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder>() {

    // PUBLIC_INTERFACE
    class MediaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.ivCardImage)
        val cardTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
        val cardDescription: TextView = itemView.findViewById(R.id.tvCardDescription)
        val cardYear: TextView = itemView.findViewById(R.id.tvCardYear)
        val cardRating: TextView = itemView.findViewById(R.id.tvCardRating)
        var watchlistBtn: Button? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_media_card, parent, false)

        val holder = MediaItemViewHolder(v)
        // Dynamically add a button for watchlist toggle if requested and not already in layout
        if (showWatchlistButton) {
            val btn = Button(parent.context).apply {
                textSize = 11f
                text = "Watchlist"
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            // Add to the card (into layout)
            (v as? ViewGroup)?.addView(btn)
            holder.watchlistBtn = btn
        }
        return holder
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val item = items[position]
        holder.cardTitle.text = item.title
        holder.cardDescription.text = item.description
        holder.cardYear.text = item.releaseYear
        holder.cardRating.text = "★ %.1f".format(item.rating)
        holder.cardImage.setImageResource(R.drawable.ic_launcher_background)
        holder.itemView.setOnClickListener { onItemClick?.invoke(item) }

        // Add/remove watchlist logic
        if (showWatchlistButton) {
            val user = AuthRepository.getCurrentUser()
            val inWatchlist = user != null && WatchlistRepository.isInWatchlist(user, item.id)
            holder.watchlistBtn?.let { btn ->
                btn.text = if (inWatchlist) "Remove from Watchlist" else "Add to Watchlist"
                btn.setOnClickListener {
                    onWatchlistToggle?.invoke(item, !inWatchlist)
                }
                btn.isEnabled = user != null
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
