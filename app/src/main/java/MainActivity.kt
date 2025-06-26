package com.smartkitchen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.smartkitchen.ui.*
import com.smartkitchen.ui.theme.SmartKitchenTheme

data class NavItem(val route: String, val label: String, val icon: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartKitchenTheme {
                val navController = rememberNavController()
                val items = listOf(
                    NavItem("ingredients", "Ingredients", "🥕"),
                    NavItem("recipes", "Recipes", "📖"),
                    NavItem("favorites", "Favorites", "❤️"),
                    NavItem("barcode", "Scan", "📷")
                )
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentRoute = navController
                                .currentBackStackEntryAsState()
                                .value?.destination?.route
                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Text(item.icon) },
                                    label = { Text(item.label) },
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        if (currentRoute != item.route) {
                                            // If navigating to recipes tab, set a flag to trigger API call
                                            if (item.route == "recipes") {
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "shouldFetchRecipes",
                                                    true
                                                )
                                            }

                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "ingredients",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("ingredients") { IngredientsScreen(navController) }
                        composable("recipes") { RecipeSwipeScreen(navController) }
                        composable("favorites") { FavoritesScreen(navController) }
                        composable("barcode") { BarcodeScannerScreen(navController) }
                    }
                }
            }
        }
    }
}
