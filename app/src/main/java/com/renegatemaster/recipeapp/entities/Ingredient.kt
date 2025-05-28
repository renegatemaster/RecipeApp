package com.renegatemaster.recipeapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable