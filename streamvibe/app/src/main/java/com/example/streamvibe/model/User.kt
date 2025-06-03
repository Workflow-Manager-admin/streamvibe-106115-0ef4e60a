package com.example.streamvibe.model

// PUBLIC_INTERFACE
data class User(
    val id: String,
    var email: String,
    var name: String,
    var avatarUrl: String? = null,
    var password: String = "",
    var watchlist: MutableList<String> = mutableListOf() // store MediaItem ids
)
