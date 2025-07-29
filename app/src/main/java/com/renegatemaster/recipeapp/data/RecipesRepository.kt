package com.renegatemaster.recipeapp.data

import android.util.Log
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val service: RecipeApiService,
) {

    private val ioDispatcher: CoroutineContext = Dispatchers.IO

    suspend fun getRecipeByFavorite(): List<Recipe> =
        withContext(ioDispatcher) {
            recipesDao.getFavoriteRecipes()
        }

    suspend fun insertRecipeIntoCache(recipe: Recipe) =
        withContext(ioDispatcher) {
            recipesDao.insertAll(recipe)
        }

    suspend fun updateRecipeFavoriteStatus(id: Int, isFavorite: Boolean) =
        withContext(ioDispatcher) {
            recipesDao.updateFavoriteStatus(id, isFavorite)
        }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe> =
        withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(categoryId).ifEmpty {
                val recipes = getRecipesByCategoryId(categoryId)
                    ?: throw Exception("Ошибка получения данных")
                insertRecipesListIntoCache(recipes, categoryId)
                recipes
            }
        }

    suspend fun insertRecipesListIntoCache(recipes: List<Recipe>, categoryId: Int) =
        withContext(ioDispatcher) {
            val recipesWithCategory = recipes.map { recipe ->
                recipe.copy(categoryId = categoryId)
            }
            recipesDao.insertAll(*recipesWithCategory.toTypedArray())
        }

    suspend fun getCategoryByIdFromCache(categoryId: Int): Category =
        withContext(ioDispatcher) {
            val categoryCache = categoriesDao.getById(categoryId)
            if (categoryCache == null) {
                val category = getCategoryById(categoryId)
                    ?: throw Exception("Ошибка получения данных")
                insertCategoriesIntoCache(listOf(category))
                category
            } else categoryCache
        }

    suspend fun getCategoriesFromCache(): List<Category> =
        withContext(ioDispatcher) {
            categoriesDao.getAll().ifEmpty {
                val categories = getCategories()
                    ?: throw Exception("Ошибка получения данных")
                insertCategoriesIntoCache(categories)
                categories
            }
        }

    suspend fun insertCategoriesIntoCache(categories: List<Category>) =
        withContext(ioDispatcher) {
            categoriesDao.insertAll(*categories.toTypedArray())
        }

    suspend fun getRecipeById(id: Int): Recipe? =
        withContext(ioDispatcher) {
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
        withContext(ioDispatcher) {
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
        withContext(ioDispatcher) {
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
        withContext(ioDispatcher) {
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
        withContext(ioDispatcher) {
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