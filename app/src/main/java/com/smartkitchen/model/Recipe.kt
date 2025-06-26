
package com.smartkitchen.model

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String,
    val rating: Float,
    val duration: String,
    val instructions: String,
    val ingredients: List<String>
)
