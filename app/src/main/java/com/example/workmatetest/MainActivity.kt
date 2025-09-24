package com.example.workmatetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workmatetest.data.local.db.UserDatabase
import com.example.workmatetest.presentation.ui.screen.RecipeDetailScreen
import com.example.workmatetest.presentation.ui.screen.RecipesScreen
import com.example.workmatetest.presentation.ui.screen.SplashScreen
import com.example.workmatetest.presentation.ui.screen.UserCreationScreen
import com.example.workmatetest.presentation.ui.theme.WorkmateTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Синхронная проверка юзера в onCreate
        val db = UserDatabase.getDatabase(this)
        val startDestination = runBlocking {
            withContext(Dispatchers.IO) {
                val user = db.userDao().getUser()
                if (user != null) "splash" else "userCreation"
            }
        }

        setContent {
            WorkmateTestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = startDestination) {
                        composable("splash") { SplashScreen(navController) }
                        composable("recipes") { RecipesScreen(navController) }
                        composable("userCreation") { UserCreationScreen(navController) }
                        composable("recipeDetail/{recipeId}") { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt() ?: 0
                            RecipeDetailScreen(navController, recipeId)
                        }
                    }
                }
            }
        }
    }
}