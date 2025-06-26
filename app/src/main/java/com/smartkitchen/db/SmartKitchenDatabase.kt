
package com.smartkitchen.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smartkitchen.model.*

@Database(entities = [Ingredient::class, FavoriteRecipe::class], version = 1)
abstract class SmartKitchenDatabase : RoomDatabase() {
    abstract fun smartKitchenDao(): SmartKitchenDao
}
