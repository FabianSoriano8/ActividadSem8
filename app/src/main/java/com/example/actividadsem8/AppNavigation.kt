package com.example.actividadsem8

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividadsem8.screens.AuthScreen
import com.example.actividadsem8.screens.HomeScreen

@Composable
fun AppNavigation(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        when (isLoggedIn) {
            true -> {
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }
                    popUpTo("check") { inclusive = true }
                }
            }
            false -> {
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                    popUpTo("check") { inclusive = true }
                }
            }
            null -> {} // Sigue en check
        }
    }

    NavHost(navController = navController, startDestination = "check") {
        composable("check") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        composable("auth") {
            AuthScreen(viewModel)
        }
        composable("home") {
            HomeScreen(viewModel)
        }
    }
}
