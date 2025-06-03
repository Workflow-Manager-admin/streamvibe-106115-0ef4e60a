package com.example.streamvibe.repository

import com.example.streamvibe.model.User

/**
 * PUBLIC_INTERFACE
 * Singleton repository to manage user watchlist.
 */
object WatchlistRepository {
    fun getUserWatchlist(user: User): List<String> = user.watchlist

    fun addToWatchlist(user: User, mediaId: String) {
        if (!user.watchlist.contains(mediaId)) {
            user.watchlist.add(mediaId)
        }
    }

    fun removeFromWatchlist(user: User, mediaId: String) {
        user.watchlist.remove(mediaId)
    }

    fun isInWatchlist(user: User, mediaId: String): Boolean {
        return user.watchlist.contains(mediaId)
    }
}
