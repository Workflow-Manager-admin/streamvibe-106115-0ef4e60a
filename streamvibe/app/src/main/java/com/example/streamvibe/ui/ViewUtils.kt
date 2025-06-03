package com.example.streamvibe.ui

import android.content.res.Resources

// PUBLIC_INTERFACE
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
