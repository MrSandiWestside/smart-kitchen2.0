
package com.smartkitchen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartkitchen.model.Ingredient
import com.smartkitchen.repository.SmartKitchenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class IngredientsViewModel(private val repository: SmartKitchenRepository) : ViewModel() {
    val ingredients = repository.getAllIngredients()
        .map { it.sortedBy { ing -> ing.name } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.insertIngredient(ingredient)
        }
    }

    fun removeIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.deleteIngredient(ingredient)
        }
    }
}
