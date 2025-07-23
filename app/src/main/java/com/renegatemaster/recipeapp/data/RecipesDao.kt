package com.renegatemaster.recipeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.renegatemaster.recipeapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg recipes: Recipe)
}