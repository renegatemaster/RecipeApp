package com.renegatemaster.recipeapp.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.renegatemaster.recipeapp.data.STUB
import com.renegatemaster.recipeapp.model.Category

class CategoriesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
    )

    private var _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    fun init() {
        Log.i("!!!", "${CategoriesListViewModel::class.simpleName} initialization")
        loadCategories()
    }

    private fun loadCategories() {
        // TODO("load from network")

        val categoriesList = STUB.getCategories()

        val currentState = categoriesListState.value ?: CategoriesListState()

        _categoriesListState.value = currentState.copy(
            categoriesList = categoriesList,
        )
    }
}