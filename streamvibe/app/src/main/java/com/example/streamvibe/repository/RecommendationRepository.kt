package com.example.streamvibe.repository

import com.example.streamvibe.model.Recommendation
import com.example.streamvibe.model.ContentRepository
import com.example.streamvibe.model.User

/**
 * PUBLIC_INTERFACE
 * Returns personalized and trending recommendations for a user (mock logic).
 */
object RecommendationRepository {
    fun getTrending(): Recommendation {
        val trendingMedia = ContentRepository.getCategories()
            .find { it.id == "trending" }
            ?.mediaIds ?: emptyList()
        return Recommendation(userId = null, mediaIds = trendingMedia)
    }

    fun getForUser(user: User?): Recommendation {
        // Simple personalized logic: recommend genres from watchlist or fallback to trending
        user ?: return getTrending()
        val lastWatchedGenres = user.watchlist
            .mapNotNull { mid -> ContentRepository.getMediaById(mid)?.genreIds }
            .flatten()
            .distinct()
        val recommended = ContentRepository.getAllMedia().filter { item ->
            item.genreIds.any { lastWatchedGenres.contains(it) } && !user.watchlist.contains(item.id)
        }.take(6).map { it.id }
        return Recommendation(userId = user.id, mediaIds = recommended.ifEmpty { getTrending().mediaIds })
    }
}
