package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.renegatemaster.recipeapp.R
import com.renegatemaster.recipeapp.databinding.FragmentFavoritesBinding
import com.renegatemaster.recipeapp.ui.recipes.recipe.RecipeFragment
import com.renegatemaster.recipeapp.ui.recipes.recipe_list.RecipesListAdapter

class FavoritesFragment: Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        with (binding) {
            val adapter = RecipesListAdapter()
            adapter.setOnItemClickListener(
                object : RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        openRecipeByRecipeId(recipeId)
                    }
                }
            )
            rvFavoriteRecipes.adapter = adapter
            rvFavoriteRecipes.isVisible = false
            clNoFavorites.isVisible = true
        }

        viewModel.favoritesState.observe(viewLifecycleOwner) { favoritesState ->
            with (binding) {
                if (favoritesState.favoritesList.isNotEmpty()) {
                    clNoFavorites.isVisible = false
                    rvFavoriteRecipes.isVisible = true
                    (rvFavoriteRecipes.adapter as RecipesListAdapter).dataSet = favoritesState.favoritesList
                } else {
                    rvFavoriteRecipes.isVisible = false
                    clNoFavorites.isVisible = true
                }
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf("ARG_RECIPE_ID" to recipeId)

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}