package com.renegatemaster.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.renegatemaster.recipeapp.databinding.FragmentListCategoriesBinding

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

        val categoryName = category.id
        val categoryImageUrl = category.imageUrl

        object BundleKeys {
            const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
            const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
            const val ARG_CATEGORY_IMAGE_URL = "ARG_CATEGORY_IMAGE_URL"
        }

        val bundle = Bundle().apply {
            putInt(BundleKeys.ARG_CATEGORY_ID, categoryId)
            putInt(BundleKeys.ARG_CATEGORY_NAME, categoryName)
            putString(BundleKeys.ARG_CATEGORY_IMAGE_URL, categoryImageUrl)
        }

        this.activity?.supportFragmentManager?.commit {
            replace<RecipesListFragment>(R.id.mainContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}