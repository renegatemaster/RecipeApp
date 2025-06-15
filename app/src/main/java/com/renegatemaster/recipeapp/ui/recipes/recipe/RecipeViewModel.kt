package com.renegatemaster.recipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renegatemaster.recipeapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 3,
        val isInFavorites: Boolean = false,
    )

    private var _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState

    fun init() {
        Log.i("!!!", "${RecipeViewModel::class.simpleName} initialization")
        _recipeState.value = _recipeState.value?.copy(isInFavorites = false) ?: RecipeState()
    }
}