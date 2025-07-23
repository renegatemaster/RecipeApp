package com.renegatemaster.recipeapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Entity
@TypeConverters(Converters::class)
@Serializable
@Parcelize
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
    @Transient val categoryId: Int? = null,
    @ColumnInfo(name = "isFavorite") @Transient val isFavorite: Boolean = false,
) : Parcelable

class Converters {
    private val json = Json { }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? =
        list?.let { json.encodeToString(it) }

    @TypeConverter
    fun toStringList(data: String?): List<String>? =
        data?.let { json.decodeFromString<List<String>>(it) }

    @TypeConverter
    fun fromIngredientList(list: List<Ingredient>?): String? =
        list?.let { json.encodeToString(it) }

    @TypeConverter
    fun toIngredientList(data: String?): List<Ingredient>? =
        data?.let { json.decodeFromString<List<Ingredient>>(it) }
}
