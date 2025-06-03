package com.example.streamvibe.model

// PUBLIC_INTERFACE
data class MediaItem(
    val id: String,
    val title: String,
    val description: String,
    val genreIds: List<String>,
    val imageUrl: String,
    val videoUrl: String,
    val releaseYear: String,
    val rating: Double,
    val actors: List<String>,
    val isTrending: Boolean = false
)
