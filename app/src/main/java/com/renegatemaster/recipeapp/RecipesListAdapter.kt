package com.renegatemaster.recipeapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renegatemaster.recipeapp.databinding.ItemRecipeBinding
import com.renegatemaster.recipeapp.entities.Recipe

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

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
            val drawable = try {
                root.context.assets.open(recipe.imageUrl).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            } catch (e: Exception) {
                Log.d("!!!", "Image not found: ${recipe.imageUrl}", e)
                null
            }
            ivItemRecipe.setImageDrawable(drawable)
            tvItemRecipeName.text = recipe.title
            cvItemRecipe.setOnClickListener {
                itemClickListener?.onItemClick(recipe.id)
            }
        }
    }
}