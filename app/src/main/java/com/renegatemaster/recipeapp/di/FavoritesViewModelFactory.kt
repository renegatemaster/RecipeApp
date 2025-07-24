package com.renegatemaster.recipeapp.di

import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val repo: RecipesRepository
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(repo)
    }

}