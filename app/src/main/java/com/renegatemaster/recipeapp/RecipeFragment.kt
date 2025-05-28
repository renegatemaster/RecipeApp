package com.renegatemaster.recipeapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.renegatemaster.recipeapp.databinding.FragmentRecipeBinding
import com.renegatemaster.recipeapp.entities.Recipe

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable("ARG_RECIPE", Recipe::class.java)
        } else {
            bundle?.getParcelable("ARG_RECIPE")
        }

        binding.tvRecipeTitle.text = recipe?.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}