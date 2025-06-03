package com.example.streamvibe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamvibe.R
import com.example.streamvibe.model.Category

/**
 * PUBLIC_INTERFACE
 * Adapter for displaying a horizontal list of categories.
 * Categories usually group media (e.g., Trending, Sci-Fi, Comedy).
 */
class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: ((Category) -> Unit)? = null
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // PUBLIC_INTERFACE
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.name.text = category.name
        holder.itemView.setOnClickListener { onCategoryClick?.invoke(category) }
    }

    override fun getItemCount(): Int = categories.size
}
