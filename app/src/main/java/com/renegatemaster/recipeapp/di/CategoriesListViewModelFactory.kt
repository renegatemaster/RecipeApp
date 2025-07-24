package com.renegatemaster.recipeapp.di

import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val repo: RecipesRepository
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(repo)
    }

}