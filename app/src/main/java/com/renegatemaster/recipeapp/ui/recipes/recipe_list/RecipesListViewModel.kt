package com.renegatemaster.recipeapp.ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe

class RecipesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe> = emptyList(),
        val recipesImage: Drawable? = null
    )

    private var _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    fun init(categoryId: Int) {
        Log.i("!!!", "${RecipesListViewModel::class.simpleName} initialization")
        loadRecipesList(categoryId)
    }

    private fun loadRecipesList(categoryId: Int) {
        // TODO("load from network")

        val category = STUB
            .getCategories()
            .firstOrNull() { it.id == categoryId }

        val recipesList = STUB
            .getRecipesByCategoryId(categoryId)

        val drawable = try {
            category?.imageUrl?.let {
                application.assets.open(it).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            }
        } catch (e: Exception) {
            Log.d("!!!", "Image not found ${category?.imageUrl}", e)
            null
        }

        val currentState = recipesListState.value ?: RecipesListState()

        _recipesListState.value = currentState.copy(
            category = category,
            recipesList = recipesList,
            recipesImage = drawable,
        )
    }
}