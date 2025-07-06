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

    private val repo = RecipesRepository()

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
    )

    private var _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun init() {
        Log.i("!!!", "${CategoriesListViewModel::class.simpleName} initialization")
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categoriesList = repo.getCategories()
            if (categoriesList == null) {
                _errorMessage.postValue(Event("Ошибка получения данных"))
                return@launch
            }

            val currentState = categoriesListState.value ?: CategoriesListState()

            val newState = currentState.copy(
                categoriesList = categoriesList,
            )

            _categoriesListState.postValue(newState)
        }
    }
}