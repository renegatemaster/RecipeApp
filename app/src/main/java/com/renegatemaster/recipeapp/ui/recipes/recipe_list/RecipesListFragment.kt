package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.renegatemaster.recipeapp.databinding.FragmentListRecipesBinding
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.utils.GlideConfig

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not be null")

    private var category: Category? = null
    private val viewModel: RecipesListViewModel by viewModels()
    private val adapter = RecipesListAdapter()
    private val args: RecipesListFragmentArgs by navArgs()

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

        category = args.category

        category?.id?.let { viewModel.init(it) }
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
                Glide
                    .with(this@RecipesListFragment)
                    .load(recipesListState.imageUrl)
                    .apply(GlideConfig.sharedOptions)
                    .into(ivRecipes)
                tvRecipesTitle.text = recipesListState.category?.title
                adapter.dataSet = recipesListState.recipesList
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
        val action = RecipesListFragmentDirections
            .actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}