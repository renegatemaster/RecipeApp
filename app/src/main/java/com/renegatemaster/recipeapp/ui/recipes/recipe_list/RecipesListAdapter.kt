package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renegatemaster.recipeapp.databinding.ItemRecipeBinding
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.GlideConfig

class RecipesListAdapter() : RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    var dataSet: List<Recipe> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecipeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        with(holder.binding) {
            val imageUrl = "${Constants.BASE_URL}${Constants.IMAGES_ENDPOINT}${recipe.imageUrl}"
            Glide
                .with(holder.itemView.context)
                .load(imageUrl)
                .apply(GlideConfig.sharedOptions)
                .into(ivItemRecipe)
            tvItemRecipeName.text = recipe.title
            cvItemRecipe.setOnClickListener {
                itemClickListener?.onItemClick(recipe.id)
            }
        }
    }
}