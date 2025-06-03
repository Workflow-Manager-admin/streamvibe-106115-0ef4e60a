package com.example.streamvibe.model

// PUBLIC_INTERFACE
data class Recommendation(
    val userId: String?, // null for trending
    val mediaIds: List<String>
)
