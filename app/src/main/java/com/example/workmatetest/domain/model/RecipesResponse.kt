package com.example.workmatetest.domain.model

data class RecipesResponse(
    val recipes: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val servings: Int,
    val readyInMinutes: Int,
    val extendedIngredients: List<Ingredient>? = null
)

data class Ingredient(
    val original: String,
    val amount: Double,
    val unit: String
)