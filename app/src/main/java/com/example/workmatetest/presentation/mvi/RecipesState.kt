package com.example.workmatetest.presentation.mvi

import com.example.workmatetest.domain.model.Recipe

data class RecipesState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedRecipe: Recipe? = null,
    val errorMessage: String? = null,
    val userName: String = ""
)