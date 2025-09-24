package com.example.workmatetest.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workmatetest.presentation.mvi.RecipeDetailIntent
import com.example.workmatetest.presentation.mvi.RecipeDetailReducer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(navController: NavController, recipeId: Int) {
    val reducer = hiltViewModel<RecipeDetailReducer>()
    val state by reducer.state.collectAsState()

    LaunchedEffect(recipeId) {
        reducer.processIntent(RecipeDetailIntent(recipeId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рецепт") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        state.recipe?.let { recipe ->
            Column(modifier = Modifier.padding(paddingValues)) {
                AsyncImage(
                    model = recipe.image,
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = recipe.title,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        } ?: CircularProgressIndicator()
    }
}