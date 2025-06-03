@file:JvmName("ActivityMainBinding")
package com.example.streamvibe.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.streamvibe.R

// Simplified manual ViewBinding for activity_main.xml
class ActivityMainBinding(
    val root: View,
    val topAppBar: MaterialToolbar,
    val fragmentContainer: FrameLayout,
    val bottomNav: BottomNavigationView
) {
    companion object {
        fun inflate(inflater: LayoutInflater): ActivityMainBinding {
            val root = inflater.inflate(R.layout.activity_main, null, false)
            return ActivityMainBinding(
                root,
                root.findViewById(R.id.topAppBar),
                root.findViewById(R.id.fragment_container),
                root.findViewById(R.id.bottomNav)
            )
        }
    }
}
