package com.renegatemaster.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants

class FavoritesViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
    )

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun init() {
        Log.i("!!!", "${FavoritesViewModel::class.simpleName} initialization")
        loadFavorites()
    }

    private fun loadFavorites() {
        // TODO("load from network")

        val favoritesIds = getFavorites()
        val favoritesList = STUB.getRecipesByIds(favoritesIds)

        val currentState = favoritesState.value ?: FavoritesState()

        _favoritesState.value = currentState.copy(
            favoritesList = favoritesList,
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
}