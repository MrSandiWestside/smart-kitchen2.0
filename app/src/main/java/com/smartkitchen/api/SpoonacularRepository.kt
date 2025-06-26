package com.smartkitchen.api

import com.smartkitchen.model.Ingredient
import com.smartkitchen.model.Recipe
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Repository that interacts with the Spoonacular API using Retrofit.
 */
class SpoonacularRepository {
    private val service: SpoonacularService
    private val cache = mutableMapOf<String, List<Recipe>>()

    // Replace with your actual API key or inject it via build config.
    private val apiKey = "YOUR_API_KEY"

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(SpoonacularService::class.java)
    }

    suspend fun getRecipesByIngredients(ingredients: List<Ingredient>): List<Recipe> {
        val ingredientNames = ingredients.joinToString(",") { it.name }
        cache[ingredientNames]?.let { return it }

        val response = service.searchRecipes(
            ingredients = ingredientNames,
            addInfo = true,
            number = 10,
            apiKey = apiKey
        )

        val recipes = response.results.map { dto ->
            Recipe(
                id = dto.id.toString(),
                title = dto.title,
                imageUrl = dto.image,
                rating = dto.spoonacularScore ?: 0f,
                duration = dto.readyInMinutes?.let { "$it min" } ?: "-",
                ingredients = dto.extendedIngredients?.map { it.name } ?: emptyList()
            )
        }

        cache[ingredientNames] = recipes
        return recipes
    }

    fun clearCache() {
        cache.clear()
    }
}
