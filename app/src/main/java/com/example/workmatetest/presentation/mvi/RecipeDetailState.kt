package com.example.workmatetest.presentation.mvi

import com.example.workmatetest.domain.model.Recipe

data class RecipeDetailState(
    val isLoading: Boolean = true,
    val recipe: Recipe? = null,
    val errorMessage: String? = null
)