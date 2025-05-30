package com.renegatemaster.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renegatemaster.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMethodBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val method = dataSet[position]
        with(holder.binding) {
            tvItemMethod.text = method
        }
    }
}
