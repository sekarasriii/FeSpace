package com.example.fespace

import  androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fespace.view.auth.AuthRoute
import com.example.fespace.view.admin.AdminRoute
import com.example.fespace.view.client.ClientRoute
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FeSpaceApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthRoute(navController = navController)
        }

        composable("client") {
            ClientRoute(navController = navController)
        }

        composable("admin") {
            // Provide the ViewModel here
            AdminRoute(
                navController = navController,
                adminViewModel = viewModel()
            )
        }
    }
}