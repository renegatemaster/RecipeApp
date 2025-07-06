package com.renegatemaster.recipeapp.data

import android.util.Log
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException

class RecipesRepository {
    private val contentType = "application/json; charset=UTF8".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getRecipeById(id: Int): Recipe? =
        withContext(Dispatchers.IO) {
            val recipeCall = service.getRecipeById(id)
            val recipeResponse = try {
                recipeCall.execute()
            } catch (e: IOException) {
                Log.i("!!!", "Repository error: $e")
                return@withContext null
            }
            recipeResponse.body()
        }

    suspend fun getRecipesByIds(ids: List<Int>): List<Recipe>? =
        withContext(Dispatchers.IO) {
            val recipesCall = service.getRecipesByIds(ids)
            val recipesResponse = try {
                recipesCall.execute()
            } catch (e: IOException) {
                Log.i("!!!", "Repository error: $e")
                return@withContext null
            }
            recipesResponse.body()
        }

    suspend fun getCategoryById(id: Int): Category? =
        withContext(Dispatchers.IO) {
            val categoryCall = service.getCategoryById(id)
            val categoryResponse = try {
                categoryCall.execute()
            } catch (e: IOException) {
                Log.i("!!!", "Repository error: $e")
                return@withContext null
            }
            categoryResponse.body()
        }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            val recipesCall = service.getRecipesByCategoryId(id)
            val recipesResponse = try {
                recipesCall.execute()
            } catch (e: IOException) {
                Log.i("!!!", "Repository error: $e")
                return@withContext null
            }
            recipesResponse.body()
        }

    suspend fun getCategories(): List<Category>? =
        withContext(Dispatchers.IO) {
            val categoriesCall = service.getCategories()
            val categoriesResponse = try {
                categoriesCall.execute()
            } catch (e: IOException) {
                Log.i("!!!", "Repository error: $e")
                return@withContext null
            }
            categoriesResponse.body()
        }
}