package com.renegatemaster.recipeapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
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

            with(btnAddToFavorite) {
                val favoritesIds = getFavorites()
                val inFavorites = recipe?.id.toString() in favoritesIds
                background = if (inFavorites) {
                    context?.let {
                        ContextCompat.getDrawable(it, R.drawable.ic_heart)
                    }
                } else {
                    context?.let {
                        ContextCompat.getDrawable(it, R.drawable.ic_heart_empty)
                    }
                }

                setOnClickListener {
                    if (inFavorites) {
                        background = context?.let {
                            ContextCompat.getDrawable(it, R.drawable.ic_heart_empty)
                        }
                        favoritesIds.remove(recipe?.id.toString())
                        saveFavorites(favoritesIds)
                    } else {
                        background = context?.let {
                            ContextCompat.getDrawable(it, R.drawable.ic_heart)
                        }
                        favoritesIds.add(recipe?.id.toString())
                        saveFavorites(favoritesIds)
                    }
                }
            }

            tvRecipeTitle.text = recipe?.title
            tvPortionsQuantity.text = "3"
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
            getString(R.string.sp_favorites_key), Context.MODE_PRIVATE
        ) ?: return

        with(sharedPrefs.edit()) {
            putStringSet(
                getString(R.string.sp_favorites_string_set), stringSet
            )
            apply()
        }
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = activity?.getSharedPreferences(
            getString(R.string.sp_favorites_key), Context.MODE_PRIVATE
        ) ?: return hashSetOf()

        val stringSet = sharedPrefs.getStringSet(
            getString(R.string.sp_favorites_string_set), hashSetOf<String>()
        ) ?: hashSetOf<String>()

        val result: HashSet<String> = HashSet(stringSet)

        return result
    }
}