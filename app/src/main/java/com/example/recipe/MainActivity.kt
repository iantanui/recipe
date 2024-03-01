package com.example.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recipes",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 56.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeListItem(
                        recipe = recipe,
                        onClick = { navController.navigate("recipeDetail/${recipe.name}") })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun RecipeListItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ingredients: ${recipe.ingredients.joinToString(", ")}")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(recipe: Recipe) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = recipe.name) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Ingredients")
            Text(
                text = recipe.ingredients.joinToString(","),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Instructions", style = MaterialTheme.typography.bodyMedium)
            Text(text = recipe.instructions)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeTheme {
        RecipeApp()
    }
}