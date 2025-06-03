package com.example.streamvibe.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streamvibe.R

import com.example.streamvibe.repository.ContentRepository
import com.example.streamvibe.model.MediaItem

// PUBLIC_INTERFACE
class PlayerViewModel : ViewModel() {
    var isPlaying = false
    var title: String = "Demo Video"
    var mediaItem: MediaItem? = null
    fun setup(mediaId: String?) {
        if (mediaId != null) {
            mediaItem = ContentRepository.getMediaById(mediaId)
            title = mediaItem?.title ?: "Unknown media"
        }
    }
}

// PUBLIC_INTERFACE
class PlayerFragment : Fragment() {
    private lateinit var viewModel: PlayerViewModel

    companion object {
        private const val ARG_MEDIA_ID = "media_id"

        // PUBLIC_INTERFACE
        fun newInstance(mediaId: String?): PlayerFragment {
            val fragment = PlayerFragment()
            val args = Bundle()
            args.putString(ARG_MEDIA_ID, mediaId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player, container, false)
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        val mediaId = arguments?.getString(ARG_MEDIA_ID)
        viewModel.setup(mediaId)

        // Build a mock player UI
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setPadding(0, 140, 0, 0)
        }
        val tvTitle = TextView(requireContext()).apply {
            text = viewModel.title
            textSize = 24f
            setTextColor(resources.getColor(R.color.accent))
        }
        val btnPlayPause = Button(requireContext()).apply {
            text = if (viewModel.isPlaying) "Pause" else "Play"
            setOnClickListener {
                viewModel.isPlaying = !viewModel.isPlaying
                text = if (viewModel.isPlaying) "Pause" else "Play"
                Toast.makeText(
                    context,
                    if (viewModel.isPlaying) "Playing..." else "Paused!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        layout.addView(tvTitle)
        layout.addView(btnPlayPause)
        (root as? FrameLayout)?.addView(layout)
        return root
    }
}
