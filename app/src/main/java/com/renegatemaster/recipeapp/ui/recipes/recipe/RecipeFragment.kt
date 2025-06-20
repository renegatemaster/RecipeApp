package com.renegatemaster.recipeapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.renegatemaster.recipeapp.R
import com.renegatemaster.recipeapp.databinding.FragmentRecipeBinding
import com.renegatemaster.recipeapp.ui.IngredientsAdapter
import com.renegatemaster.recipeapp.ui.MethodAdapter

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private var recipeId: Int = 0
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            recipeId = it.getInt("ARG_RECIPE_ID", 0)
        }

        viewModel.init(recipeId)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        setDivider(binding.rvIngredients)
        setDivider(binding.rvMethods)

        viewModel.recipeState.observe(viewLifecycleOwner) { recipeState ->
            with(binding) {
                recipeState.recipe?.let {
                    recipeId = it.id
                    rvIngredients.adapter = IngredientsAdapter(it.ingredients)
                    rvMethods.adapter = MethodAdapter(it.method)
                }
                ivRecipe.setImageDrawable(recipeState.recipeImage)

                with(btnAddToFavorite) {
                    if (recipeState.isInFavorites) {
                        setFavoriteButtonBackground(R.drawable.ic_heart)
                    } else setFavoriteButtonBackground(R.drawable.ic_heart_empty)

                    setOnClickListener {
                        viewModel.onFavoritesClicked(recipeId)
                    }
                }

                tvRecipeTitle.text = recipeState.recipe?.title
                tvPortionsQuantity.text = recipeState.portionsCount.toString()

                sbPortionsQuantity.setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            viewModel.updatePortionsCount(progress)
                            (rvIngredients.adapter as? IngredientsAdapter)?.updateIngredients(progress)
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        }
                    }
                )
            }
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

    private fun setFavoriteButtonBackground(drawableResId: Int) {
        try {
            with(binding.btnAddToFavorite) {
                ContextCompat.getDrawable(context, drawableResId)?.let { drawable: Drawable ->
                    background = drawable
                }
            }
        } catch (e: Exception) {
            Log.d("!!!", "Can't set background image with id $drawableResId", e)
        }
    }
}