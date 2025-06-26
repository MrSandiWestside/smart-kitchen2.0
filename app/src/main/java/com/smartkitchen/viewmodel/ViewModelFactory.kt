package com.smartkitchen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.smartkitchen.db.SmartKitchenDatabase
import com.smartkitchen.repository.SmartKitchenRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
<<<<<<< codex/barcode-scanner-integrieren
    private val database: SmartKitchenDatabase by lazy {
        Room.databaseBuilder(
=======
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = Room.databaseBuilder(
>>>>>>> main
            context.applicationContext,
            SmartKitchenDatabase::class.java,
            "smart_kitchen.db"
        ).build()
<<<<<<< codex/barcode-scanner-integrieren
    }

    private val repository by lazy { SmartKitchenRepository(database.smartKitchenDao()) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(IngredientsViewModel::class.java) -> {
            IngredientsViewModel(repository) as T
        }
        modelClass.isAssignableFrom(RecipesViewModel::class.java) -> {
            RecipesViewModel(repository) as T
        }
        else -> throw IllegalArgumentException("Unknown ViewModel class")
=======
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
>>>>>>> main
    }
}
