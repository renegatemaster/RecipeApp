package com.renegatemaster.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.Event
import java.util.concurrent.Executors

class RecipeViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository()
    private val threadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS)

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 3,
        val isInFavorites: Boolean = false,
        val recipeImage: Drawable? = null
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
        threadPool.execute {
            val recipe = repo.getRecipeById(recipeId)
            if (recipe == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@execute
            }
            val currentState = recipeState.value ?: RecipeState()
            val drawable = try {
                application.assets.open(recipe.imageUrl).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${recipe.imageUrl}", e)
                null
            }

            val newState = currentState.copy(
                recipe = recipe,
                portionsCount = currentState.portionsCount,
                isInFavorites = recipeId.toString() in getFavorites(),
                recipeImage = drawable,
            )

            _recipeState.postValue(newState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }

    fun onFavoritesClicked(recipeId: Int) {
        val favoritesIds = getFavorites()
        if (recipeState.value?.isInFavorites == true) {
            _recipeState.value = recipeState.value?.copy(
                isInFavorites = false
            )
            favoritesIds.remove(recipeId.toString())
        } else {
            _recipeState.value = recipeState.value?.copy(
                isInFavorites = true
            )
            favoritesIds.add(recipeId.toString())
        }
        saveFavorites(favoritesIds)
    }

    fun updatePortionsCount(portionsCount: Int) {
        _recipeState.value = recipeState.value?.copy(
            portionsCount = portionsCount,
        )
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = application.getSharedPreferences(
            Constants.SP_FAVORITES_KEY, Context.MODE_PRIVATE
        ) ?: return hashSetOf()

        val stringSet = sharedPrefs.getStringSet(
            Constants.SP_FAVORITES_STRING_SET, hashSetOf<String>()
        ) ?: hashSetOf<String>()

        val result: HashSet<String> = HashSet(stringSet)

        return result
    }

    private fun saveFavorites(stringSet: Set<String>) {
        val sharedPrefs = application.getSharedPreferences(
            Constants.SP_FAVORITES_KEY, Context.MODE_PRIVATE
        ) ?: return

        with(sharedPrefs.edit()) {
            putStringSet(
                Constants.SP_FAVORITES_STRING_SET, stringSet
            )
            apply()
        }
    }
}