package com.example.streamvibe.repository

import com.example.streamvibe.model.*

/**
 * PUBLIC_INTERFACE
 * Singleton repository for all content data (media, genres, categories, etc.)
 */
object ContentRepository {
    private val mockGenres = listOf(
        Genre("action", "Action"),
        Genre("comedy", "Comedy"),
        Genre("drama", "Drama"),
        Genre("sci-fi", "Sci-Fi"),
        Genre("horror", "Horror"),
        Genre("romance", "Romance")
    )

    private val mockMedia = listOf(
        MediaItem(
            id = "101",
            title = "The Last Hero",
            description = "An epic adventure of a reluctant hero.",
            genreIds = listOf("action", "drama"),
            imageUrl = "https://placehold.jp/600x900.png?text=The+Last+Hero",
            videoUrl = "https://www.html5rocks.com/en/tutorials/video/basics/devstories.webm",
            releaseYear = "2022",
            rating = 7.9,
            actors = listOf("John Doe", "Jane Smith"),
            isTrending = true
        ),
        MediaItem(
            id = "102",
            title = "Laugh Riot",
            description = "A rib-tickling comedy for the whole family.",
            genreIds = listOf("comedy"),
            imageUrl = "https://placehold.jp/600x900.png?text=Laugh+Riot",
            videoUrl = "https://www.html5rocks.com/en/tutorials/video/basics/devstories.webm",
            releaseYear = "2021",
            rating = 8.3,
            actors = listOf("Funny Guy", "Comic Lady"),
            isTrending = true
        ),
        MediaItem(
            id = "103",
            title = "Space Odyssey",
            description = "Journey across the stars.",
            genreIds = listOf("sci-fi", "drama"),
            imageUrl = "https://placehold.jp/600x900.png?text=Space+Odyssey",
            videoUrl = "https://www.html5rocks.com/en/tutorials/video/basics/devstories.webm",
            releaseYear = "2023",
            rating = 8.7,
            actors = listOf("Captain Star", "Alien Queen"),
            isTrending = false
        )
        // Add more as needed
    )

    private val mockCategories = listOf(
        Category(
            id = "trending",
            name = "Trending",
            mediaIds = mockMedia.filter { it.isTrending }.map { it.id }
        ),
        Category(
            id = "sci-fi",
            name = "Sci-Fi",
            mediaIds = mockMedia.filter { it.genreIds.contains("sci-fi") }.map { it.id }
        ),
        Category(
            id = "comedy",
            name = "Comedy",
            mediaIds = mockMedia.filter { it.genreIds.contains("comedy") }.map { it.id }
        )
    )

    fun getGenres(): List<Genre> = mockGenres

    fun getAllMedia(): List<MediaItem> = mockMedia

    fun getMediaById(id: String): MediaItem? = mockMedia.find { it.id == id }

    fun getCategories(): List<Category> = mockCategories

    fun searchMedia(query: String): List<MediaItem> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()
        return mockMedia.filter {
            it.title.lowercase().contains(q) ||
                    it.actors.any { actor -> actor.lowercase().contains(q) } ||
                    it.genreIds.any { gid -> mockGenres.find { g -> g.id == gid }?.name?.lowercase()?.contains(q) == true }
        }
    }
}
