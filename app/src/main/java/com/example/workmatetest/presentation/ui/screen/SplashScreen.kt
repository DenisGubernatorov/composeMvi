package com.example.workmatetest.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workmatetest.presentation.mvi.SplashIntent
import com.example.workmatetest.presentation.mvi.SplashReducer

@Composable
fun SplashScreen(navController: NavController) {
    val reducer = hiltViewModel<SplashReducer>()
    val state = reducer.state.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.value.isLoading) {
            CircularProgressIndicator()
        }

        state.value.errorMessage?.let { message ->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = message)
                    Button(
                        onClick = {
                            navController.navigate("recipes") {
                                popUpTo("splash") { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("OK")
                    }
                }
            }
            LaunchedEffect(Unit) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        } ?: run {
            if (!state.value.isLoading) {
                LaunchedEffect(Unit) {
                    navController.navigate("recipes") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        reducer.processIntent(SplashIntent.SyncData)
    }
}