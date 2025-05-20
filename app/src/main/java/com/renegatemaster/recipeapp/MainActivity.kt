package com.renegatemaster.recipeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.renegatemaster.recipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnCategories.setOnClickListener {
                fragmentManager.commit {
                    replace<CategoriesListFragment>(R.id.mainContainer)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }

            btnFavorites.setOnClickListener {
                fragmentManager.commit {
                    replace<FavoritesFragment>(R.id.mainContainer)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }
}