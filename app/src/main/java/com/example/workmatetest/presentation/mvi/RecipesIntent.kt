package com.example.workmatetest.presentation.mvi

import com.example.workmatetest.domain.model.Recipe

sealed class RecipesIntent {
    data class SearchRecipes(val query: String) : RecipesIntent()
    object LoadRecipes : RecipesIntent()
    data class SelectRecipe(val recipe: Recipe) : RecipesIntent()
    object LoadUserName : RecipesIntent()
}