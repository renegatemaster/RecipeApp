package com.renegatemaster.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isInFavorites: Boolean = false,
    )

    private val _recipeState = MutableStateFlow(RecipeState())
    val recipeState = _recipeState.asStateFlow()

    fun getRecipe(recipeId: Int) {
        _recipeState.update { currentState ->
            currentState.copy(
                recipe = STUB.getRecipeById(recipeId)
            )
        }
    }

    fun toggleFavorites() {
        _recipeState.update { currentState ->
            currentState.copy(
                isInFavorites = !currentState.isInFavorites
            )
        }
    }
}