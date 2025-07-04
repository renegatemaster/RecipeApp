package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import com.renegatemaster.recipeapp.utils.Event
import java.util.concurrent.Executors

class RecipesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository()
    private val threadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS)

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe> = emptyList(),
        val recipesImage: Drawable? = null
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
        threadPool.execute {
            val category = repo.getCategoryById(categoryId)
            val recipesList = repo.getRecipesByCategoryId(categoryId)
            if (category == null || recipesList == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@execute
            }

            val drawable = try {
                application.assets.open(category.imageUrl).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${category.imageUrl}", e)
                null
            }

            val currentState = recipesListState.value ?: RecipesListState()

            val newState = currentState.copy(
                category = category,
                recipesList = recipesList,
                recipesImage = drawable,
            )

            _recipesListState.postValue(newState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}