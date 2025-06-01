package com.renegatemaster.recipeapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renegatemaster.recipeapp.databinding.ItemIngredientBinding
import com.renegatemaster.recipeapp.entities.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 3

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
            val overallIngredientQuantity = ingredient.quantity.toBigDecimal() * quantity.toBigDecimal()
            val ingredientQuantity = if (
                overallIngredientQuantity % 1.toBigDecimal() != 0.0.toBigDecimal()
                ) {
                "%.1f".format(overallIngredientQuantity)
            } else {
                overallIngredientQuantity.toInt().toString()
            }
            tvItemIngredientQuantity.text = "$ingredientQuantity ${ingredient.unitOfMeasure}"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}