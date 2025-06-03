package com.example.streamvibe.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
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

    var currentPosition: Int = 0 // In seconds
    var duration: Int = 0 // In seconds (simulate: e.g. 2 min = 120)
    private var onPositionUpdateListener: (() -> Unit)? = null
    private var handler: Handler? = null

    // PUBLIC_INTERFACE
    fun setup(mediaId: String?, onPositionUpdate: (() -> Unit)?) {
        if (mediaId != null) {
            mediaItem = ContentRepository.getMediaById(mediaId)
            title = mediaItem?.title ?: "Unknown media"
            // Simulate duration for each media (use a mock value, e.g., 91..180 seconds)
            duration = 120 + (mediaItem?.id?.hashCode() ?: 0) % 61 // between 120-180
        } else {
            title = "Demo Video"
            duration = 150
        }
        currentPosition = 0
        this.onPositionUpdateListener = onPositionUpdate
        handler = Handler(Looper.getMainLooper())
    }

    // PUBLIC_INTERFACE
    fun play() {
        if (!isPlaying) {
            isPlaying = true
            startTimer()
        }
    }

    // PUBLIC_INTERFACE
    fun pause() {
        isPlaying = false
    }

    // PUBLIC_INTERFACE
    fun seekTo(position: Int) {
        currentPosition = position.coerceIn(0, duration)
        onPositionUpdateListener?.invoke()
    }

    // Start a timer to update currentPosition while playing
    private fun startTimer() {
        handler?.post(object : Runnable {
            override fun run() {
                if (isPlaying) {
                    currentPosition += 1
                    if (currentPosition >= duration) {
                        currentPosition = duration
                        isPlaying = false
                    }
                    onPositionUpdateListener?.invoke()
                    if (isPlaying) {
                        handler?.postDelayed(this, 1000)
                    }
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        handler?.removeCallbacksAndMessages(null)
    }
}

// PUBLIC_INTERFACE
class PlayerFragment : Fragment() {
    private lateinit var viewModel: PlayerViewModel
    private var seekBar: SeekBar? = null
    private var playPauseButton: Button? = null
    private var tvCurrent: TextView? = null
    private var tvDuration: TextView? = null

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
        
        // Simulate video player UI
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setPadding(40, 100, 40, 0)
        }

        val tvTitle = TextView(requireContext()).apply {
            text = ""
            textSize = 24f
            setTextColor(resources.getColor(R.color.accent))
            setPadding(0, 12, 0, 12)
        }
        layout.addView(tvTitle)

        seekBar = SeekBar(requireContext())
        seekBar?.max = 100
        layout.addView(seekBar)

        // Time display (current/duration)
        val timeLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER
        }
        tvCurrent = TextView(requireContext()).apply {
            text = "00:00"
            setPadding(0, 0, 15, 0)
            setTextColor(resources.getColor(R.color.accent))
        }
        tvDuration = TextView(requireContext()).apply {
            text = "00:00"
            setPadding(15, 0, 0, 0)
            setTextColor(resources.getColor(R.color.accent))
        }
        timeLayout.addView(tvCurrent)
        timeLayout.addView(TextView(requireContext()).apply {
            text = " / "
            setTextColor(resources.getColor(R.color.accent))
        })
        timeLayout.addView(tvDuration)
        layout.addView(timeLayout)

        // Start/Pause Button
        playPauseButton = Button(requireContext()).apply {
            text = "Play"
            setOnClickListener {
                if (viewModel.isPlaying) {
                    viewModel.pause()
                } else {
                    viewModel.play()
                }
                updateUI()
            }
        }
        layout.addView(playPauseButton)

        // Seek backward/forward controls
        val seekButtonsLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER
        }
        val btnRewind = Button(requireContext()).apply {
            text = "<< 10s"
            setOnClickListener {
                viewModel.seekTo(viewModel.currentPosition - 10)
                updateUI()
            }
        }
        val btnForward = Button(requireContext()).apply {
            text = "10s >>"
            setOnClickListener {
                viewModel.seekTo(viewModel.currentPosition + 10)
                updateUI()
            }
        }
        seekButtonsLayout.addView(btnRewind)
        seekButtonsLayout.addView(btnForward)
        layout.addView(seekButtonsLayout)

        // Watchlist Button
        val btnWatchlist = Button(requireContext()).apply {
            val user = com.example.streamvibe.repository.AuthRepository.getCurrentUser()
            val inWatchlist = user != null && viewModel.mediaItem?.id?.let { com.example.streamvibe.repository.WatchlistRepository.isInWatchlist(user, it) } ?: false
            text = if (inWatchlist) "Remove from Watchlist" else "Add to Watchlist"
            isEnabled = user != null && viewModel.mediaItem != null
            setOnClickListener {
                val user = com.example.streamvibe.repository.AuthRepository.getCurrentUser()
                val mediaId = viewModel.mediaItem?.id
                if (user != null && mediaId != null) {
                    val wasInWatchlist = com.example.streamvibe.repository.WatchlistRepository.isInWatchlist(user, mediaId)
                    if (wasInWatchlist) {
                        com.example.streamvibe.repository.WatchlistRepository.removeFromWatchlist(user, mediaId)
                        text = "Add to Watchlist"
                        Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
                    } else {
                        com.example.streamvibe.repository.WatchlistRepository.addToWatchlist(user, mediaId)
                        text = "Remove from Watchlist"
                        Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        layout.addView(btnWatchlist)
        (root as? FrameLayout)?.addView(layout)

        // Setup ViewModel and callback for position update
        viewModel.setup(mediaId) {
            updateUI()
        }
        tvTitle.text = viewModel.title

        // Set up SeekBar max/duration/time
        seekBar?.max = viewModel.duration
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: Pause during drag
                if (viewModel.isPlaying) {
                    viewModel.pause()
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekTo(progress)
                    updateUI()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: auto-resume
                // viewModel.play()
            }
        })
        updateUI()
        return root
    }

    private fun updateUI() {
        seekBar?.progress = viewModel.currentPosition
        seekBar?.max = viewModel.duration
        playPauseButton?.text = if (viewModel.isPlaying) "Pause" else "Play"
        tvCurrent?.text = formatTime(viewModel.currentPosition)
        tvDuration?.text = formatTime(viewModel.duration)
    }

    private fun formatTime(sec: Int): String {
        val min = sec / 60
        val s = sec % 60
        return "%02d:%02d".format(min, s)
    }
}
