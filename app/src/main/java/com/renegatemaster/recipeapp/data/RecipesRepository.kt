package com.renegatemaster.recipeapp.data

import android.util.Log
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException

class RecipesRepository {
    private val contentType = "application/json; charset=UTF8".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun getRecipeById(id: Int): Recipe? {
        val recipeCall = service.getRecipeById(id)
        val recipeResponse = try {
            recipeCall.execute()
        } catch (e: IOException) {
            Log.i("!!!", "Repository error: $e")
            return null
        }
        val recipe = recipeResponse.body()
        return recipe
    }

    fun getRecipesByIds(ids: List<Int>): List<Recipe>? {
        val recipesCall = service.getRecipesByIds(ids)
        val recipesResponse = try {
            recipesCall.execute()
        } catch (e: IOException) {
            Log.i("!!!", "Repository error: $e")
            return null
        }
        val recipes = recipesResponse.body()
        return recipes
    }

    fun getCategoryById(id: Int): Category? {
        val categoryCall = service.getCategoryById(id)
        val categoryResponse = try {
            categoryCall.execute()
        } catch (e: IOException) {
            Log.i("!!!", "Repository error: $e")
            return null
        }
        val category = categoryResponse.body()
        return category
    }

    fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        val recipesCall = service.getRecipesByCategoryId(id)
        val recipesResponse = try {
            recipesCall.execute()
        } catch (e: IOException) {
            Log.i("!!!", "Repository error: $e")
            return null
        }
        val recipes = recipesResponse.body()
        return recipes
    }

    fun getCategories(): List<Category>? {
        val categoriesCall = service.getCategories()
        val categoriesResponse = try {
            categoriesCall.execute()
        } catch (e: IOException) {
            Log.i("!!!", "Repository error: $e")
            return null
        }
        val categories = categoriesResponse.body()
        return categories
    }
}