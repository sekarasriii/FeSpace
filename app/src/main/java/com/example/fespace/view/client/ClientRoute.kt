package com.example.fespace.view.client

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController

@Composable
fun ClientRoute(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "client_home"
    ) {

        composable("client_home") {
            ClientHomeScreen(
                onOrderClick = {
                    navController.navigate("order_service")
                }
            )
        }

        composable("order_service") {
            OrderServiceScreen(
                onOrderSuccess = {
                    navController.navigate("order_status")
                }
            )
        }

        composable("order_status") {
            OrderStatusScreen()
        }
    }
}
