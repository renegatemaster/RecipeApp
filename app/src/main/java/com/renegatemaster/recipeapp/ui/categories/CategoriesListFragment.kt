package com.renegatemaster.recipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.renegatemaster.recipeapp.R
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.databinding.FragmentListCategoriesBinding
import com.renegatemaster.recipeapp.ui.recipes.recipe_list.RecipesListFragment

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
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
        val adapter = CategoriesListAdapter(STUB.getCategories())
        adapter.setOnItemClickListener(
            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            }
        )
        binding.rvCategories.adapter = adapter
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB
            .getCategories()
            .firstOrNull() { it.id == categoryId }
            ?: return

        val categoryName = category.title
        val categoryImageUrl = category.imageUrl

        val bundle = bundleOf(
            "ARG_CATEGORY_ID" to categoryId,
            "ARG_CATEGORY_NAME" to categoryName,
            "ARG_CATEGORY_IMAGE_URL" to categoryImageUrl,
        )

        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}