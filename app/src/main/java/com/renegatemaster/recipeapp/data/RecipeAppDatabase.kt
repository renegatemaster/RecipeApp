package com.renegatemaster.recipeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.renegatemaster.recipeapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class RecipeAppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}