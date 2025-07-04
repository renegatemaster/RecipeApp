package com.renegatemaster.recipeapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renegatemaster.recipeapp.databinding.ItemIngredientBinding
import com.renegatemaster.recipeapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter() : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var dataSet: List<Ingredient> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
            if (ingredient.quantity.toBigDecimalOrNull() != null) {
                val ingredientQuantity = BigDecimal(ingredient.quantity)
                    .multiply(BigDecimal(quantity))
                    .setScale(1, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString()
                tvItemIngredientQuantity.text = "$ingredientQuantity ${ingredient.unitOfMeasure}"
            } else {
                tvItemIngredientQuantity.text = "${ingredient.quantity} ${ingredient.unitOfMeasure}"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}