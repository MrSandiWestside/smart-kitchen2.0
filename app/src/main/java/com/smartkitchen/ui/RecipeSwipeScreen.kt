
package com.smartkitchen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.smartkitchen.model.Recipe
import com.smartkitchen.viewmodel.IngredientsViewModel
import com.smartkitchen.viewmodel.RecipesViewModel
import com.smartkitchen.viewmodel.ViewModelFactory

@Composable
fun RecipeSwipeScreen(
    navController: NavController,
    viewModel: RecipesViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    ),
    ingredientsViewModel: IngredientsViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    )
) {
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val ingredients by ingredientsViewModel.ingredients.collectAsState()

    // Get the saved state handle to check if we should fetch recipes
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val shouldFetchRecipes = savedStateHandle?.get<Boolean>("shouldFetchRecipes") ?: false

    // Effect to fetch recipes when navigating to this screen with the flag set
    LaunchedEffect(shouldFetchRecipes) {
        if (shouldFetchRecipes && ingredients.isNotEmpty()) {
            // Clear the flag to prevent fetching again
            savedStateHandle?.set("shouldFetchRecipes", false)

            // Fetch recipes based on ingredients
            viewModel.fetchRecipesByIngredients(ingredients)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Swipe Recipes", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            // Show loading indicator
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (recipes.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { recipes.size })

            HorizontalPager(state = pagerState) { page ->
                val recipe = recipes[page]
                RecipeCard(recipe = recipe)
            }

            Spacer(modifier = Modifier.height(12.dp))
        } else {
            // No recipes found
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                if (ingredients.isEmpty()) {
                    Text("Add ingredients to get recipe suggestions", style = MaterialTheme.typography.bodyLarge)
                } else {
                    Text("No recipes found for your ingredients", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(recipe.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(recipe.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))
            Text("⏱ ${recipe.duration} | ⭐ ${recipe.rating}", modifier = Modifier.padding(horizontal = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(recipe.instructions, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}
