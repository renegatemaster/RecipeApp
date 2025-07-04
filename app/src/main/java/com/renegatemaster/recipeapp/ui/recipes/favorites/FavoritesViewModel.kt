package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.Event
import java.util.concurrent.Executors

class FavoritesViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository()
    private val threadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS)

    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
    )

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun init() {
        Log.i("!!!", "${FavoritesViewModel::class.simpleName} initialization")
        loadFavorites()
    }

    private fun loadFavorites() {
        val favoritesIds: List<Int> = getFavorites()
        threadPool.execute {
            val favoritesList = repo.getRecipesByIds(favoritesIds)
            if (favoritesList == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@execute
            }

            val currentState = favoritesState.value ?: FavoritesState()

            val newState = currentState.copy(
                favoritesList = favoritesList,
            )

            _favoritesState.postValue(newState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }

    private fun getFavorites(): List<Int> {
        val sharedPrefs = application.getSharedPreferences(
            Constants.SP_FAVORITES_KEY, Context.MODE_PRIVATE
        ) ?: return emptyList()

        val stringSet = sharedPrefs.getStringSet(
            Constants.SP_FAVORITES_STRING_SET, hashSetOf<String>()
        ) ?: hashSetOf<String>()

        val result = stringSet.map { it.toInt() }.toList()

        return result
    }
}