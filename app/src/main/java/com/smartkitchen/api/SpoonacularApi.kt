package com.smartkitchen.api

import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/findByIngredients")
    suspend fun findByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): List<IngredientRecipeDto>
}

data class IngredientRecipeDto(
    val id: Int,
    val title: String,
    val image: String
)
