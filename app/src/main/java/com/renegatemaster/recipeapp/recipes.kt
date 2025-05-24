package com.renegatemaster.recipeapp

import com.renegatemaster.recipeapp.entities.Category

object STUB {
    private val categories: List<Category> = listOf(
        Category(
            0,
            "Бургеры",
            "Рецепты всех популярных видов бургеров",
            "burger1.png"
        ),
        Category(
            1,
            "Десерты",
            "Самые вкусные рецепты десертов специально для вас",
            "dessert.png"
        ),
        Category(
            2,
            "Пицца",
            "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            "pizza.png",
        ),
        Category(
            3,
            "Рыба",
            "Печеная, жареная, сушеная, любая рыба на твой вкус",
            "fish.png",
        ),
        Category(
            4,
            "Супы",
            "От классики до экзотики: мир в одной тарелке",
            "soup.png",
        ),
        Category(
            5,
            "Салаты",
            "Хрустящий калейдоскоп под соусом вдохновения",
            "salad.png",
        )
    )

    fun getCategories(): List<Category> = categories
}