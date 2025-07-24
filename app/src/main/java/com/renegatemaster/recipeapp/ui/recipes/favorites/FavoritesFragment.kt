package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.renegatemaster.recipeapp.RecipeApplication
import com.renegatemaster.recipeapp.databinding.FragmentFavoritesBinding
import com.renegatemaster.recipeapp.ui.recipes.recipe_list.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    private lateinit var viewModel: FavoritesViewModel
    private val adapter = RecipesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        viewModel = appContainer.favoritesViewModelFactory.create()
    }

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
        with(binding) {
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
            with(binding) {
                if (favoritesState.favoritesList.isNotEmpty()) {
                    clNoFavorites.isVisible = false
                    rvFavoriteRecipes.isVisible = true
                    adapter.dataSet = favoritesState.favoritesList
                } else {
                    rvFavoriteRecipes.isVisible = false
                    clNoFavorites.isVisible = true
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { msg ->
                Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = FavoritesFragmentDirections
            .actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}