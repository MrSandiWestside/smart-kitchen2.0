package com.smartkitchen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.smartkitchen.db.SmartKitchenDatabase
import com.smartkitchen.repository.SmartKitchenRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = Room.databaseBuilder(
            context.applicationContext,
            SmartKitchenDatabase::class.java,
            "smart_kitchen.db"
        ).build()
        val repository = SmartKitchenRepository(database.smartKitchenDao())
        return when {
            modelClass.isAssignableFrom(IngredientsViewModel::class.java) -> {
                IngredientsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RecipesViewModel::class.java) -> {
                RecipesViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
