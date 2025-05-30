package com.renegatemaster.recipeapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.renegatemaster.recipeapp.databinding.FragmentRecipeBinding
import com.renegatemaster.recipeapp.entities.Recipe

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private var recipe: Recipe? = null

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
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable("ARG_RECIPE", Recipe::class.java)
        } else {
            bundle?.getParcelable("ARG_RECIPE")
        }

        initUI()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        with(binding) {
            val drawable = try {
                recipe?.imageUrl?.let {
                    root.context.assets.open(it).use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
                }
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${recipe?.imageUrl}", e)
                null
            }

            ivRecipe.setImageDrawable(drawable)
            tvRecipeTitle.text = recipe?.title
        }
    }

    private fun initRecycler() {
        with(binding) {
            recipe?.let {
                rvIngredients.adapter = IngredientsAdapter(it.ingredients)
                rvMethods.adapter = MethodAdapter(it.method)
            }
            setDivider(rvIngredients)
            setDivider(rvMethods)
        }
    }

    private fun setDivider(rv: RecyclerView) {
        with(rv) {
            val divider = MaterialDividerItemDecoration(
                context,
                MaterialDividerItemDecoration.VERTICAL,
            )
            divider.setDividerColorResource(context, R.color.dividerColor)
            divider.setDividerInsetStartResource(context, R.dimen.basicIndentation)
            divider.setDividerInsetEndResource(context, R.dimen.basicIndentation)
            divider.isLastItemDecorated = false
            addItemDecoration(divider)
        }
    }
}