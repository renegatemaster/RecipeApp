package com.renegatemaster.recipeapp.data

import android.content.Context
import android.util.Log
import androidx.room.Room
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

class RecipesRepository(context: Context) {
    private val contentType = "application/json; charset=UTF8".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val db: RecipeAppDatabase = Room.databaseBuilder(
        context.applicationContext,
        RecipeAppDatabase::class.java,
        "database-name",
        )
        .fallbackToDestructiveMigration(false)
        .build()

    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.recipesDao()

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe> =
        withContext(Dispatchers.IO) {
            recipesDao.getRecipesByCategoryId(categoryId).ifEmpty {
                val recipes = getRecipesByCategoryId(categoryId)
                    ?: throw Exception("Ошибка получения данных")
                insertRecipesListIntoCache(recipes, categoryId)
                recipes
            }
        }

    suspend fun insertRecipesListIntoCache(recipes: List<Recipe>, categoryId: Int) =
        withContext(Dispatchers.IO) {
            val recipesWithCategory = recipes.map { recipe ->
                recipe.copy(categoryId = categoryId)
            }
            recipesDao.insertAll(*recipesWithCategory.toTypedArray())
        }

    suspend fun getCategoryByIdFromCache(categoryId: Int): Category =
        withContext(Dispatchers.IO) {
            val categoryCache = categoriesDao.getById(categoryId)
            if (categoryCache == null) {
                val category = getCategoryById(categoryId)
                    ?: throw Exception("Ошибка получения данных")
                insertCategoriesIntoCache(listOf(category))
                category
            } else categoryCache
        }

    suspend fun getCategoriesFromCache(): List<Category> =
        withContext(Dispatchers.IO) {
            categoriesDao.getAll().ifEmpty {
                val categories = getCategories()
                    ?: throw Exception("Ошибка получения данных")
                insertCategoriesIntoCache(categories)
                categories
            }
        }

    suspend fun insertCategoriesIntoCache(categories: List<Category>) =
        withContext(Dispatchers.IO) {
            categoriesDao.insertAll(*categories.toTypedArray())
        }

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