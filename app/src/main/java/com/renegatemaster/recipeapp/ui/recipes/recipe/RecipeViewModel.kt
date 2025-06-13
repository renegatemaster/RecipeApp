package com.renegatemaster.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.renegatemaster.recipeapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 3,
        val isInFavorites: Boolean = false,
    )
}