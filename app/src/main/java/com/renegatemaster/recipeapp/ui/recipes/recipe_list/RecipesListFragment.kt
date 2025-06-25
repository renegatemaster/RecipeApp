package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.renegatemaster.recipeapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not be null")

    private var categoryId: Int = 0
    private val viewModel: RecipesListViewModel by viewModels()
    private val adapter = RecipesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            categoryId = it.getInt("ARG_CATEGORY_ID", 0)
        }

        viewModel.init(categoryId)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        adapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
        binding.rvRecipes.adapter = adapter

        viewModel.recipesListState.observe(viewLifecycleOwner) { recipesListState ->
            with(binding) {
                ivRecipes.setImageDrawable(recipesListState.recipesImage)
                tvRecipesTitle.text = recipesListState.category?.title
                adapter.dataSet = recipesListState.recipesList
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}