package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
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

class FavoritesViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository(application)

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
        viewModelScope.launch {
            val favoritesList = repo.getRecipesByIds(favoritesIds)
            if (favoritesList == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@launch
            }

            val currentState = favoritesState.value ?: FavoritesState()

            val newState = currentState.copy(
                favoritesList = favoritesList,
            )

            _favoritesState.postValue(newState)
        }
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