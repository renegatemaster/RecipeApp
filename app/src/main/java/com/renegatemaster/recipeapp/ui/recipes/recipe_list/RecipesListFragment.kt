package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.renegatemaster.recipeapp.R
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.databinding.FragmentListRecipesBinding
import com.renegatemaster.recipeapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not be null")

    private var argCategoryId: Int? = null
    private var argCategoryName: String? = null
    private var argCategoryImageUrl: String? = null

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

        val bundle = arguments
        argCategoryId = bundle?.getInt("ARG_CATEGORY_ID", 0)
        argCategoryName = bundle?.getString("ARG_CATEGORY_NAME")
        argCategoryImageUrl = bundle?.getString("ARG_CATEGORY_IMAGE_URL")

        initRecycler()

        val drawable = try {
            argCategoryImageUrl?.let {
                binding.root.context.assets.open(it).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            }
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: $argCategoryImageUrl", e)
            null
        }
        with(binding) {
            ivRecipes.setImageDrawable(drawable)
            tvRecipesTitle.text = argCategoryName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val adapter = argCategoryId?.let {
            RecipesListAdapter(STUB.getRecipesByCategoryId(it))
        } ?: return
        adapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
        binding.rvRecipes.adapter = adapter
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