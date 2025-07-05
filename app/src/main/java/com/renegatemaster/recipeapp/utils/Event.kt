package com.renegatemaster.recipeapp.utils

class Event<out T>(private val content: T) {
    private var handled = false

    fun getContentIfNotHandled(): T? =
        if (handled) null
        else {
            handled = true
            content
        }
}
