package com.example.workmatetest.presentation.mvi

data class UserCreationState(
    val surname: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)