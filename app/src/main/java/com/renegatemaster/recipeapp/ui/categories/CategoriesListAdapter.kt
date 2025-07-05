package com.renegatemaster.recipeapp.ui.categories

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renegatemaster.recipeapp.databinding.ItemCategoryBinding
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.GlideConfig

class CategoriesListAdapter() : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    var dataSet: List<Category> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

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
            val imageUrl = "${Constants.BASE_URL}${Constants.IMAGES_ENDPOINT}${category.imageUrl}"
            Glide
                .with(holder.itemView.context)
                .load(imageUrl)
                .apply(GlideConfig.sharedOptions)
                .into(ivItemCategory)
            tvItemCategoryName.text = category.title
            tvItemCategoryDescription.text = category.description
            cvItemCategory.setOnClickListener {
                itemClickListener?.onItemClick(category.id)
            }
        }
    }
}