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
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")
    private val threadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            val body = Json.decodeFromString<List<Category>>(
                connection.inputStream.bufferedReader().readText()
            )
            Log.i("!!!", "Body: $body")

            val catsIds = body.map { it.id }
            catsIds.forEach { id ->
                threadPool.execute {
                    val url = URL(
                        "https://recipes.androidsprint.ru/api/category/$id/recipes"
                    )
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connect()
                    val recipes = Json.decodeFromString<List<Recipe>>(
                        connection.inputStream.bufferedReader().readText()
                    )
                    Log.i("!!!", "Recipes: $recipes")
                }
            }
            threadPool.shutdown()
        }
        thread.start()

        with(binding) {
            btnCategories.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
            }

            btnFavorites.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
            }
        }
    }
}