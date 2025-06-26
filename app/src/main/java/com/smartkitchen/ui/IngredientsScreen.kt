package com.smartkitchen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.smartkitchen.model.Category
import com.smartkitchen.model.Ingredient
import com.smartkitchen.viewmodel.IngredientsViewModel
import com.smartkitchen.viewmodel.ViewModelFactory

@Composable
fun IngredientsScreen(navController: NavController, viewModel: IngredientsViewModel = viewModel(
    factory = ViewModelFactory(LocalContext.current)
)) {
    val ingredients by viewModel.ingredients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Ingredient")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("My Ingredients", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                items(ingredients) { ingredient ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(ingredient.name, style = MaterialTheme.typography.titleMedium)
                            Text("${ingredient.quantity} ${ingredient.unit} (${ingredient.category})")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("shouldFetchRecipes", true)
                    navController.navigate("recipes")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Rezepte finden")
            }
        }

        if (showDialog) {
            AddIngredientDialog(
                onDismiss = { showDialog = false },
                onAdd = { viewModel.addIngredient(it) }
            )
        }
    }
}
