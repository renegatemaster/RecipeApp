package com.renegatemaster.recipeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.renegatemaster.recipeapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category")
    suspend fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE id = :categoryId LIMIT 1")
    suspend fun getById(categoryId: Int): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg categories: Category)
}