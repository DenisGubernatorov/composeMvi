package com.example.workmatetest.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workmatetest.presentation.mvi.UserCreationIntent
import com.example.workmatetest.presentation.mvi.UserCreationReducer

@Composable
fun UserCreationScreen(
    navController: NavController,
    reducer: UserCreationReducer = hiltViewModel()
) {
    val state by reducer.state.collectAsState()
    val navigationEvent by reducer.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { destination ->
            navController.navigate(destination) {
                popUpTo("userCreation") { inclusive = true }
            }
            reducer.clearNavigationEvent()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Создание профиля")
        OutlinedTextField(
            value = state.surname,
            onValueChange = { reducer.processIntent(UserCreationIntent.UpdateSurname(it)) },
            label = { Text("Фамилия") }
        )
        OutlinedTextField(
            value = state.name,
            onValueChange = { reducer.processIntent(UserCreationIntent.UpdateName(it)) },
            label = { Text("Имя") }
        )
        if (state.isLoading) CircularProgressIndicator()
        state.errorMessage?.let { Text(it, color = androidx.compose.ui.graphics.Color.Red) }
        Button(
            onClick = { reducer.processIntent(UserCreationIntent.SaveUser) },
            enabled = state.surname.isNotEmpty() && state.name.isNotEmpty()
        ) {
            Text("Сохранить")
        }
    }
}