package com.renegatemaster.recipeapp.di

import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.ui.recipes.recipe_list.RecipesListViewModel

class RecipesListViewModelFactory(
    private val repo: RecipesRepository
) : Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(repo)
    }

}