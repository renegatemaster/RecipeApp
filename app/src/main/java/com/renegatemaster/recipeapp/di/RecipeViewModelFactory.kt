package com.renegatemaster.recipeapp.di

import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val repo: RecipesRepository
) : Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(repo)
    }

}