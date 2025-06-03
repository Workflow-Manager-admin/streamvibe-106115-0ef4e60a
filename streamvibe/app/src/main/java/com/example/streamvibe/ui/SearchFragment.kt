package com.example.streamvibe.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamvibe.R
import com.example.streamvibe.model.MediaItem
import com.example.streamvibe.repository.ContentRepository
import com.example.streamvibe.ui.adapter.MediaItemAdapter

// PUBLIC_INTERFACE
class SearchViewModel : ViewModel() {
    var query: String = ""
    var results: List<MediaItem> = emptyList()
    fun search(query: String) {
        this.query = query
        results = ContentRepository.searchMedia(query)
    }
}

// PUBLIC_INTERFACE
class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchBox: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MediaItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        // Add a search box at the top 
        val rootLayout = FrameLayout(requireContext())
        searchBox = EditText(requireContext()).apply {
            hint = "Search by title, genre, actor..."
            setTextColor(resources.getColor(R.color.text_primary))
            setHintTextColor(resources.getColor(R.color.text_secondary))
            setPadding(30, 36, 30, 36)
        }
        recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(context)
        }

        rootLayout.addView(searchBox)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.topMargin = 0
        recyclerView.layoutParams = lp
        rootLayout.addView(recyclerView)

        adapter = MediaItemAdapter(emptyList()) { item ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlayerFragment.newInstance(item.id))
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString() ?: ""
                viewModel.search(q)
                adapter = MediaItemAdapter(viewModel.results) { item ->
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PlayerFragment.newInstance(item.id))
                        .addToBackStack(null)
                        .commit()
                }
                recyclerView.adapter = adapter
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return rootLayout
    }
}
