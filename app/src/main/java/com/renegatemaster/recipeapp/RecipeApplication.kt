package com.renegatemaster.recipeapp

import android.app.Application
import com.renegatemaster.recipeapp.di.AppContainer

class RecipeApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}