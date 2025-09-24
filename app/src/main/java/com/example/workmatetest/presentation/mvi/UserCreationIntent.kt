package com.example.workmatetest.presentation.mvi

sealed class UserCreationIntent {
    data class UpdateSurname(val surname: String) : UserCreationIntent()
    data class UpdateName(val name: String) : UserCreationIntent()
    object SaveUser : UserCreationIntent()
}