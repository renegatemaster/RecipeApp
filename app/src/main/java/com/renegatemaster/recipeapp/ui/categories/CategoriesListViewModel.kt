package com.renegatemaster.recipeapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val repo: RecipesRepository,
) : ViewModel() {

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