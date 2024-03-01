package com.example.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipe.ui.theme.RecipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeApp()
                }
            }
        }
    }
}

@Composable
fun RecipeApp() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        NavHost(navController = navController, startDestination = "recipeList") {
            composable("recipeList") {
                RecipeListScreen(navController = navController)
            }
            composable(
                "recipeDetail/{recipeName}",
                arguments = listOf(navArgument("recipeName") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeName = backStackEntry.arguments?.getString("recipeName")
                val recipe = recipes.find { it.name == recipeName }
                if (recipe != null) {
                    RecipeDetailScreen(recipe = recipe)
                } else {
                    Text(text = "Recipe not found")
                }
            }
        }
    }
}

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
)

val recipes = listOf(
    Recipe("Pasta", listOf("Pasta", "Tomato Sauce"), "Cook pasta and mix with tomato sauce."),
    Recipe("Salad", listOf("Lettuce", "Tomato", "Cucumber"), "Chop vegetables and mix in a bowl.")
)

@Composable
fun RecipeListScreen(navController: NavHostController) {
    Column {
        for (recipe in recipes) {
            RecipeListItem(
                recipe = recipe,
                onClick = { navController.navigate("recipeDetail/${recipe.name}") })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RecipeListItem(recipe: Recipe, onClick: () -> Unit) {
    Text(
        text = recipe.name,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .clickable(onClick = onClick)
    )
}

@Composable
fun RecipeDetailScreen(recipe: Recipe) {
    Column {
        Text(text = recipe.name, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Ingredients: ${recipe.ingredients.joinToString(",")}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Instructions: ${recipe.instructions}")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeTheme {
        RecipeApp()
    }
}