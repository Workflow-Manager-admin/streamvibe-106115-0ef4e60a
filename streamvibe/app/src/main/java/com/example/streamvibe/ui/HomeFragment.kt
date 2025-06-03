package com.example.streamvibe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamvibe.R
import com.example.streamvibe.model.Category
import com.example.streamvibe.model.MediaItem
import com.example.streamvibe.repository.ContentRepository
import com.example.streamvibe.repository.RecommendationRepository
import com.example.streamvibe.repository.AuthRepository
import com.example.streamvibe.ui.adapter.MediaItemAdapter
import com.example.streamvibe.ui.adapter.CategoryAdapter

// PUBLIC_INTERFACE
class HomeViewModel : ViewModel() {
    val categories: List<Category> = ContentRepository.getCategories()
    val featured: MediaItem? = ContentRepository.getAllMedia().firstOrNull()
    val recommended: List<MediaItem>
    val trending: List<MediaItem>

    init {
        recommended = RecommendationRepository.getForUser(AuthRepository.getCurrentUser())
            .mediaIds.mapNotNull { ContentRepository.getMediaById(it) }
        trending = ContentRepository.getCategories().find { it.id == "trending" }
            ?.mediaIds?.mapNotNull { ContentRepository.getMediaById(it) } ?: emptyList()
    }
}

// PUBLIC_INTERFACE
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Find root vertical LinearLayout in the inflated ScrollView
        val containerLayout: LinearLayout = ((root as? ViewGroup)
            ?.getChildAt(0) as? LinearLayout)
            ?: (root as? LinearLayout)
            ?: throw IllegalStateException("HomeFragment: Can't find root LinearLayout")

        // Remove all placeholder stub children
        containerLayout.removeAllViews()

        // Featured carousel (use first media)
        val featuredCard = CardView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                180.dp // fallback height if resource fails
            )
            radius = 16f
            setCardBackgroundColor(resources.getColor(R.color.secondary))
            val tv = TextView(requireContext()).apply {
                text = viewModel.featured?.title ?: "Featured"
                setTextColor(resources.getColor(R.color.text_primary))
                textSize = 20f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = android.view.Gravity.CENTER
            }
            addView(tv)
        }
        containerLayout.addView(featuredCard)

        // Categories horizontally scrolling
        for (cat in viewModel.categories) {
            val tvTitle = TextView(requireContext()).apply {
                text = cat.name
                setTextColor(resources.getColor(R.color.secondary))
                setTypeface(null, android.graphics.Typeface.BOLD)
                textSize = 18f
                setPadding(0, 18, 0, 0)
            }
            containerLayout.addView(tvTitle)

            val recyclerView = RecyclerView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.category_list_height)
                )
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val mediaList = cat.mediaIds.mapNotNull { ContentRepository.getMediaById(it) }
                adapter = MediaItemAdapter(mediaList) {
                    // on item click: show toast, in a real app go to detail/player
                    Toast.makeText(context, "Clicked: ${it.title}", Toast.LENGTH_SHORT).show()
                }
            }
            containerLayout.addView(recyclerView)
        }
        // Recommended section
        val tvRecommended = TextView(requireContext()).apply {
            text = "Recommended"
            setTextColor(resources.getColor(R.color.secondary))
            setTypeface(null, android.graphics.Typeface.BOLD)
            textSize = 18f
            setPadding(0, 18, 0, 0)
        }
        containerLayout.addView(tvRecommended)

        val recommendedRecycler = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.category_list_height)
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MediaItemAdapter(viewModel.recommended) {
                Toast.makeText(context, "Clicked: ${it.title}", Toast.LENGTH_SHORT).show()
            }
        }
        containerLayout.addView(recommendedRecycler)

        return root
    }
}
