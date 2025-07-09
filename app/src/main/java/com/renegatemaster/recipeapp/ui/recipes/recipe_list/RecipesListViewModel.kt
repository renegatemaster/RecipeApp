package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.Event
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository()

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe> = emptyList(),
        val imageUrl: String? = null
    )

    private var _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun init(categoryId: Int) {
        Log.i("!!!", "${RecipesListViewModel::class.simpleName} initialization")
        loadRecipesList(categoryId)
    }

    private fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val category = repo.getCategoryById(categoryId)
            val recipesList = repo.getRecipesByCategoryId(categoryId)
            if (category == null || recipesList == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@launch
            }
            val imageUrl = "${Constants.BASE_URL}${Constants.IMAGES_ENDPOINT}${category.imageUrl}"
            val currentState = recipesListState.value ?: RecipesListState()

            val newState = currentState.copy(
                category = category,
                recipesList = recipesList,
                imageUrl = imageUrl,
            )

            _recipesListState.postValue(newState)
        }
    }
}