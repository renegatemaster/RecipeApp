package com.renegatemaster.recipeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.renegatemaster.recipeapp.R
import com.renegatemaster.recipeapp.databinding.ActivityMainBinding
import com.renegatemaster.recipeapp.model.Category
import com.renegatemaster.recipeapp.model.Recipe
import com.renegatemaster.recipeapp.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val threadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS)
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        threadPool.execute {
            val requestCategories = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()
            var categories: List<Category>

            client.newCall(requestCategories).execute().use { response ->
                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                Log.i("!!!", "responseCode: ${response.code}")
                Log.i("!!!", "responseMessage: ${response.message}")
                categories = Json.decodeFromString<List<Category>>(
                    response.body?.string() ?: ""
                )
                Log.i("!!!", "Body: $categories")
            }

            val catsIds = categories.map { it.id }
            catsIds.forEach { id ->
                val requestRecipes = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category/$id/recipes")
                    .build()
                client.newCall(requestRecipes).execute().use { response ->
                    val recipes = Json.decodeFromString<List<Recipe>>(
                        response.body?.string() ?: ""
                    )
                    Log.i("!!!", "Recipes: $recipes")
                }
            }
        }

        with(binding) {
            btnCategories.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
            }

            btnFavorites.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}