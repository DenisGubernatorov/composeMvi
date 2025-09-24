package com.example.workmatetest.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.workmatetest.presentation.mvi.RecipesIntent
import com.example.workmatetest.presentation.mvi.RecipesReducer
import com.example.workmatetest.presentation.ui.component.RecipeCard
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(navController: NavController, reducer: RecipesReducer = hiltViewModel()) {
    val state by reducer.state.collectAsState()
    val recipes = reducer.recipesFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        reducer.processIntent(RecipesIntent.LoadRecipes)
        reducer.processIntent(RecipesIntent.LoadUserName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рецепты") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "Привет, ${state.userName}!",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { query ->
                    reducer.processIntent(RecipesIntent.SearchRecipes(query))
                },
                label = { Text("Введите название рецепта") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Text(
                text = "Рецепты",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recipes.itemCount) { index ->
                    recipes[index]?.let { recipe ->
                        RecipeCard(recipe = recipe, onClick = {
                            reducer.processIntent(RecipesIntent.SelectRecipe(recipe))
                            navController.navigate("recipeDetail/${recipe.id}")
                        })
                    }
                }
                item {
                    when (recipes.loadState.append) {
                        is LoadState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                        is LoadState.Error -> Text("Ошибка загрузки. Проверьте соединение.")
                        else -> {}
                    }
                }
            }

            Text(
                text = "Рецепт дня",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            val randomRecipe = if (recipes.itemCount > 0) recipes[Random.nextInt(recipes.itemCount)] else null
            randomRecipe?.let { recipe ->
                Card(
                    onClick = {
                        reducer.processIntent(RecipesIntent.SelectRecipe(recipe))
                        navController.navigate("recipeDetail/${recipe.id}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .heightIn(min = 200.dp, max = 300.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    AsyncImage(
                        model = recipe.image,
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .heightIn(min = 150.dp, max = 250.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = recipe.title,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } ?: Text("Нет рецептов для показа")
        }
    }
}