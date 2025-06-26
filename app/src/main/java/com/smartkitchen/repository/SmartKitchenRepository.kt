
package com.smartkitchen.repository

import com.smartkitchen.db.SmartKitchenDao
import com.smartkitchen.model.*
import kotlinx.coroutines.flow.Flow

class SmartKitchenRepository(private val dao: SmartKitchenDao) {
    fun getAllIngredients(): Flow<List<Ingredient>> = dao.getAllIngredients()
    suspend fun insertIngredient(ingredient: Ingredient) = dao.insertIngredient(ingredient)
    suspend fun deleteIngredient(ingredient: Ingredient) = dao.deleteIngredient(ingredient)

    fun getFavoriteRecipes(): Flow<List<FavoriteRecipe>> = dao.getFavoriteRecipes()
    suspend fun insertFavorite(recipe: FavoriteRecipe) = dao.insertFavorite(recipe)
    suspend fun deleteFavorite(recipe: FavoriteRecipe) = dao.deleteFavorite(recipe)
}
