package com.renegatemaster.recipeapp.model

import android.os.Parcelable
import androidx.annotation.StyleRes
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable