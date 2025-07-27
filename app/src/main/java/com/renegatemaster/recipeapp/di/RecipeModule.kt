package com.renegatemaster.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.renegatemaster.recipeapp.data.CategoriesDao
import com.renegatemaster.recipeapp.data.RecipeApiService
import com.renegatemaster.recipeapp.data.RecipeAppDatabase
import com.renegatemaster.recipeapp.data.RecipesDao
import com.renegatemaster.recipeapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipeAppDatabase =
        Room.databaseBuilder(
            context,
            RecipeAppDatabase::class.java,
            "database-name",
        )
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideCategoriesDao(db: RecipeAppDatabase): CategoriesDao = db.categoriesDao()

    @Provides
    fun provideRecipesDao(db: RecipeAppDatabase): RecipesDao = db.recipesDao()

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val contentType = "application/json; charset=UTF8".toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(client)
            .build()
        return retrofit
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService =
        retrofit.create(RecipeApiService::class.java)
}