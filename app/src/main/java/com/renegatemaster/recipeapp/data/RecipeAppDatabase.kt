package com.renegatemaster.recipeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 2)
abstract class RecipeAppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}