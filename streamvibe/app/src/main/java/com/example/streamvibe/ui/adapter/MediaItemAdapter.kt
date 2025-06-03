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
class MediaItemAdapter(
    private val items: List<MediaItem>,
    private val onItemClick: ((MediaItem) -> Unit)? = null
) : RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder>() {

    // PUBLIC_INTERFACE
    class MediaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.ivCardImage)
        val cardTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
        val cardDescription: TextView = itemView.findViewById(R.id.tvCardDescription)
        val cardYear: TextView = itemView.findViewById(R.id.tvCardYear)
        val cardRating: TextView = itemView.findViewById(R.id.tvCardRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_media_card, parent, false)
        return MediaItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val item = items[position]
        // You can use a library like Glide/Picasso for image loading; here, we use a stub.
        // Glide.with(holder.cardImage).load(item.imageUrl).into(holder.cardImage)
        holder.cardTitle.text = item.title
        holder.cardDescription.text = item.description
        holder.cardYear.text = item.releaseYear
        holder.cardRating.text = "★ %.1f".format(item.rating)
        // TODO: Real image loading
        holder.cardImage.setImageResource(R.drawable.ic_launcher_background)

        holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
    }

    override fun getItemCount(): Int = items.size
}
