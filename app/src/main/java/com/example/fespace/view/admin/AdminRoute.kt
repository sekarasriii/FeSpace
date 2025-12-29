package com.example.fespace.view.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fespace.viewmodel.AdminViewModel // Import ini

@Composable
fun AdminRoute(
    navController: NavHostController,
    adminViewModel: AdminViewModel // Tambahkan parameter ini
) {
    NavHost(
        navController = navController,
        startDestination = "admin_dashboard"
    ) {
        composable("admin_dashboard") {
            // Kirim viewModel ke AdminDashboardScreen
            AdminDashboardScreen(adminViewModel = adminViewModel)
        }
        composable("manage_portfolio") {
            ManagePortfolioScreen() // Tambahkan viewModel di sini nanti jika perlu
        }
        composable("manage_service") {
            ManageServiceScreen()
        }
        composable("manage_order") {
            ManageOrderScreen()
        }
    }
}
