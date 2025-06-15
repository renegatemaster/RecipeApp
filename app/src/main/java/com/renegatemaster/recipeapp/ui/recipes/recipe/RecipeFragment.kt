package com.renegatemaster.recipeapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
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
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.ui.IngredientsAdapter
import com.renegatemaster.recipeapp.ui.MethodAdapter
import com.renegatemaster.recipeapp.utils.Constants

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private var recipe: Recipe? = null
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

        val bundle = arguments
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable("ARG_RECIPE", Recipe::class.java)
        } else {
            bundle?.getParcelable("ARG_RECIPE")
        }

        initUI()
        initRecycler()

        viewModel.recipeState.observe(viewLifecycleOwner) { recipeState ->
            Log.i("!!!", "isInFavorites: ${recipeState.isInFavorites}")
        }
        viewModel.init()
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

            with(btnAddToFavorite) {
                if (isInFavorites()) {
                    setFavoriteButtonBackground(R.drawable.ic_heart)
                } else setFavoriteButtonBackground(R.drawable.ic_heart_empty)

                setOnClickListener {
                    val recipeId: Int = recipe?.id ?: return@setOnClickListener
                    val favoritesIds = getFavorites()
                    if (isInFavorites()) {
                        setFavoriteButtonBackground(R.drawable.ic_heart_empty)
                        favoritesIds.remove(recipeId.toString())
                    } else {
                        setFavoriteButtonBackground(R.drawable.ic_heart)
                        favoritesIds.add(recipeId.toString())
                    }
                    saveFavorites(favoritesIds)
                }
            }

            tvRecipeTitle.text = recipe?.title
            tvPortionsQuantity.text = sbPortionsQuantity.progress.toString()
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

            sbPortionsQuantity.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        tvPortionsQuantity.text = progress.toString()
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

    private fun saveFavorites(stringSet: Set<String>) {
        val sharedPrefs = activity?.getSharedPreferences(
            Constants.SP_FAVORITES_KEY, Context.MODE_PRIVATE
        ) ?: return

        with(sharedPrefs.edit()) {
            putStringSet(
                Constants.SP_FAVORITES_STRING_SET, stringSet
            )
            apply()
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

    private fun isInFavorites(): Boolean {
        return recipe?.id?.toString() in getFavorites()
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