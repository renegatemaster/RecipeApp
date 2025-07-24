package com.renegatemaster.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.renegatemaster.recipeapp.data.RecipeApiService
import com.renegatemaster.recipeapp.data.RecipeAppDatabase
import com.renegatemaster.recipeapp.data.RecipesRepository
import com.renegatemaster.recipeapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlin.coroutines.CoroutineContext

class AppContainer(context: Context) {
    private val contentType = "application/json; charset=UTF8".toMediaType()
    private val logging = HttpLoggingInterceptor()
        .apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val db: RecipeAppDatabase = Room.databaseBuilder(
        context,
        RecipeAppDatabase::class.java,
        "database-name",
    )
        .fallbackToDestructiveMigration(false)
        .build()

    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.recipesDao()
    private val ioDispatcher: CoroutineContext = Dispatchers.IO

    private val repo = RecipesRepository(
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        service = service,
        ioDispatcher = ioDispatcher
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repo)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repo)
    val recipeViewModelFactory = RecipeViewModelFactory(repo)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repo)
}