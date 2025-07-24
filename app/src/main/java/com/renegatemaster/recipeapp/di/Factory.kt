package com.renegatemaster.recipeapp.di

interface Factory<T> {
    fun create(): T
}