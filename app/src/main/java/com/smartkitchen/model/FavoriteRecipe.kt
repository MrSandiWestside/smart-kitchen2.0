
package com.smartkitchen.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteRecipe(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val rating: Float,
    val duration: String,
    val savedAt: Long
)
