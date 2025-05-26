package com.renegatemaster.recipeapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renegatemaster.recipeapp.databinding.ItemCategoryBinding
import com.renegatemaster.recipeapp.entities.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCategoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        with(holder.binding) {
            val drawable =
                try {
                    root.context.assets.open(category.imageUrl).use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
                } catch (e: Exception) {
                    Log.d("!!!", "Image not found: ${category.imageUrl}", e)
                    null
                }
            ivItemCategory.setImageDrawable(drawable)
            tvItemCategoryName.text = category.title
            tvItemCategoryDescription.text = category.description
        }
    }
}