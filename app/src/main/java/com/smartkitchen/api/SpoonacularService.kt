package com.smartkitchen.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service definition for the Spoonacular API.
 */
interface SpoonacularService {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("includeIngredients") ingredients: String,
        @Query("addRecipeInformation") addInfo: Boolean = true,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): SpoonacularResponse
}

/** Simple DTOs matching the subset of the API response we care about. */
data class SpoonacularResponse(
    val results: List<SpoonacularRecipe>
)

data class SpoonacularRecipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int?,
    val spoonacularScore: Float?,
    val extendedIngredients: List<SpoonacularIngredient>?
)

data class SpoonacularIngredient(
    val name: String
)
