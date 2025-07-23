package com.renegatemaster.recipeapp.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.Event
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository(application)

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 3,
        val isInFavorites: Boolean = false,
        val imageUrl: String? = null
    )

    private var _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun init(recipeId: Int) {
        Log.i("!!!", "${RecipeViewModel::class.simpleName} initialization")
        loadRecipe(recipeId)
    }

    private fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipe = repo.getRecipeById(recipeId)
            val favoritesIds = repo.getRecipeByFavorite().map { it.id }
            if (recipe == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@launch
            }
            val imageUrl = "${Constants.BASE_URL}${Constants.IMAGES_ENDPOINT}${recipe.imageUrl}"
            val currentState = recipeState.value ?: RecipeState()

            val newState = currentState.copy(
                recipe = recipe,
                portionsCount = currentState.portionsCount,
                isInFavorites = recipeId in favoritesIds,
                imageUrl = imageUrl,
            )

            _recipeState.postValue(newState)
        }
    }

    fun onFavoritesClicked() {
        viewModelScope.launch {
            val state = requireNotNull(recipeState.value) {
                "${RecipeViewModel::class.simpleName}.onFavoritesClicked(): state is null"
            }
            val recipe = requireNotNull(state.recipe) {
                "${RecipeViewModel::class.simpleName}.onFavoritesClicked(): recipe is null"
            }

            val isFavorite = !state.isInFavorites
            val newRecipe = recipe.copy(
                isFavorite = isFavorite
            )
            _recipeState.value = state.copy(
                recipe = newRecipe,
                isInFavorites = isFavorite
            )
            repo.updateRecipeFavoriteStatus(recipe.id, isFavorite)
        }
    }

    fun updatePortionsCount(portionsCount: Int) {
        _recipeState.value = recipeState.value?.copy(
            portionsCount = portionsCount,
        )
    }
}