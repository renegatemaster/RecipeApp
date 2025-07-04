package com.renegatemaster.recipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.databinding.FragmentListCategoriesBinding
import com.renegatemaster.recipeapp.utils.Constants
import java.util.concurrent.Executors

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")

    private val viewModel: CategoriesListViewModel by viewModels()
    private val adapter = CategoriesListAdapter()
    private val repo = RecipesRepository()
    private val threadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS)

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
        viewModel.init()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }

    private fun initUI() {
        adapter.setOnItemClickListener(
            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            }
        )
        binding.rvCategories.adapter = adapter

        viewModel.categoriesListState.observe(viewLifecycleOwner) { categoriesListState ->
            adapter.dataSet = categoriesListState.categoriesList
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

    private fun openRecipesByCategoryId(categoryId: Int) {
        threadPool.execute {
            val category = repo
                .getCategoryById(categoryId)
                ?: throw IllegalArgumentException("Couldn't find category with provided id")
            val action = CategoriesListFragmentDirections
                .actionCategoriesListFragmentToRecipesListFragment(category)

            activity?.runOnUiThread {
                findNavController().navigate(action)
            }
        }
    }
}