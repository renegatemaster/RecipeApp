package com.renegatemaster.recipeapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renegatemaster.recipeapp.databinding.ItemIngredientBinding
import com.renegatemaster.recipeapp.entities.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemIngredientBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        with(holder.binding) {
            tvItemIngredientDescription.text = ingredient.description
            tvItemIngredientQuantity.text = "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        }
    }
}