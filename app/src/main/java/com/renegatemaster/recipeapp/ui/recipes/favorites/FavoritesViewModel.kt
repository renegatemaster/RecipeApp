package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: RecipesRepository
) : ViewModel() {

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
        viewModelScope.launch {
            val favoritesList = repo.getRecipeByFavorite()
            val currentState = favoritesState.value ?: FavoritesState()
            val newState = currentState.copy(
                favoritesList = favoritesList,
            )
            _favoritesState.postValue(newState)
        }
    }
}