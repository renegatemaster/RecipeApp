package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.renegatemaster.recipeapp.R
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.databinding.FragmentFavoritesBinding
import com.renegatemaster.recipeapp.ui.recipes.recipe.RecipeFragment
import com.renegatemaster.recipeapp.ui.recipes.recipe_list.RecipesListAdapter
import com.renegatemaster.recipeapp.utils.Constants

class FavoritesFragment: Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

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
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val favoritesIds = getFavorites()
        with (binding) {
            if (favoritesIds.size > 0) {
                clNoFavorites.isVisible = false
                rvFavoriteRecipes.isVisible = true
                val adapter = RecipesListAdapter(
                    STUB.getRecipesByIds(favoritesIds)
                )
                adapter.setOnItemClickListener(
                    object : RecipesListAdapter.OnItemClickListener {
                        override fun onItemClick(recipeId: Int) {
                            openRecipeByRecipeId(recipeId)
                        }
                    }
                )
                rvFavoriteRecipes.adapter = adapter
            } else {
                rvFavoriteRecipes.isVisible = false
                clNoFavorites.isVisible = true
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

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = activity?.getSharedPreferences(
            Constants.SP_FAVORITES_KEY, Context.MODE_PRIVATE
        ) ?: return hashSetOf()

        val stringSet = sharedPrefs.getStringSet(
            Constants.SP_FAVORITES_STRING_SET, hashSetOf<String>()
        ) ?: hashSetOf<String>()

        val result: HashSet<String> = HashSet(stringSet)

        return result
    }
}