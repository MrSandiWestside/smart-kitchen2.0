
package com.smartkitchen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartkitchen.api.SpoonacularRepository
import com.smartkitchen.model.Ingredient
import com.smartkitchen.model.Recipe
import com.smartkitchen.model.FavoriteRecipe
import com.smartkitchen.repository.SmartKitchenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class RecipesViewModel(private val repository: SmartKitchenRepository? = null) : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val spoonacularRepository = SpoonacularRepository()

    // Flag to track if we've already fetched recipes
    private var hasLoadedRecipes = false

    init {
        // Load dummy recipes as fallback
        loadDummyRecipes()
    }

    fun fetchRecipesByIngredients(ingredients: List<Ingredient>) {
        // Clear the repository cache to force a fresh fetch
        spoonacularRepository.clearCache()

        // Reset the loaded flag
        hasLoadedRecipes = false

        // Only fetch if we haven't already or if the list is empty
        if (!hasLoadedRecipes || _recipes.value.isEmpty()) {
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    val fetchedRecipes = spoonacularRepository.getRecipesByIngredients(ingredients)
                    if (fetchedRecipes.isNotEmpty()) {
                        _recipes.value = fetchedRecipes
                    } else {
                        // Fallback to dummy recipes if API returns empty
                        loadDummyRecipes()
                    }
                    hasLoadedRecipes = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Fallback to dummy recipes on error
                    loadDummyRecipes()
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun loadDummyRecipes() {
        val dummy = listOf(
            Recipe(UUID.randomUUID().toString(), "Pasta Carbonara", "https://via.placeholder.com/300", 4.5f, "20 min", listOf("Pasta", "Egg", "Bacon")),
            Recipe(UUID.randomUUID().toString(), "Tomato Soup", "https://via.placeholder.com/300", 4.2f, "30 min", listOf("Tomato", "Onion", "Cream")),
            Recipe(UUID.randomUUID().toString(), "Veggie Stir Fry", "https://via.placeholder.com/300", 4.8f, "15 min", listOf("Broccoli", "Carrot", "Soy Sauce"))
        )
        _recipes.value = dummy
    }

    fun removeTopRecipe() {
        _recipes.value = _recipes.value.drop(1)
    }

    fun saveFavorite(recipe: Recipe) {
        repository?.let {
            val favorite = FavoriteRecipe(
                id = recipe.id,
                title = recipe.title,
                imageUrl = recipe.imageUrl,
                rating = recipe.rating,
                duration = recipe.duration,
                savedAt = System.currentTimeMillis()
            )
            viewModelScope.launch {
                it.insertFavorite(favorite)
            }
        }
    }
}
