package com.smartkitchen.api

import com.smartkitchen.model.Ingredient
import com.smartkitchen.model.Recipe
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpoonacularRepository {
    private val apiKey = "DEMO_KEY" // Replace with real key if available

    private val api: SpoonacularApi

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(SpoonacularApi::class.java)
    }

    private var cache: List<Recipe>? = null

    suspend fun getRecipesByIngredients(ingredients: List<Ingredient>): List<Recipe> {
        cache?.let { return it }
        val ingredientString = ingredients.joinToString(",") { it.name }
        val dtos = api.findByIngredients(ingredientString, apiKey = apiKey)
        val recipes = dtos.map { dto ->
            Recipe(
                id = dto.id.toString(),
                title = dto.title,
                imageUrl = "https://spoonacular.com/recipeImages/${dto.id}-556x370.${dto.image.substringAfterLast('.')}",
                rating = 0f,
                duration = "", // Duration not provided
                instructions = ""
            )
        }
        cache = recipes
        return recipes
    }

    fun clearCache() {
        cache = null
    }
}
