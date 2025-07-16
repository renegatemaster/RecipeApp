package com.renegatemaster.recipeapp.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.utils.Event
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repo = RecipesRepository(application)

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
    )

    private var _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage
    private val _navigateEvent = MutableLiveData<Event<Category>>()
    val navigateEvent: LiveData<Event<Category>> = _navigateEvent

    fun init() {
        Log.i("!!!", "${CategoriesListViewModel::class.simpleName} initialization")
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categoriesListCache = repo.getCategoriesFromCache()

            val currentStateCache = categoriesListState.value ?: CategoriesListState()

            val newStateCache = currentStateCache.copy(
                categoriesList = categoriesListCache,
            )

            _categoriesListState.postValue(newStateCache)

            val categoriesList = repo.getCategories()
            if (categoriesList == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@launch
            }

            val newState = newStateCache.copy(
                categoriesList = categoriesList,
            )

            _categoriesListState.postValue(newState)
            repo.insertCategoriesIntoCache(categoriesList)
        }
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        viewModelScope.launch {
            val category = repo
                .getCategoryById(categoryId)
                ?: throw IllegalArgumentException("Couldn't find category with provided id")

            _navigateEvent.postValue(Event(category))
        }
    }
}