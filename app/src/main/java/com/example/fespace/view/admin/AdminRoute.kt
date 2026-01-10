package com.example.fespace.view.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fespace.viewmodel.AdminViewModel
import com.example.fespace.data.local.entity.OrderEntity

@Composable
fun AdminRoute(
    navController: NavHostController,
    adminViewModel: AdminViewModel
) {
    // Mengambil data orders dari ViewModel
    val orders by adminViewModel.orders.collectAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = "admin_dashboard"
    ) {
        // RUTE UTAMA ADMIN
        composable("admin_dashboard") {
            AdminDashboardScreen(
                adminViewModel = adminViewModel,
                navController = navController
            )
        }

        // RUTE DETAIL ORDER (Sesuai dengan error: membutuhkan 'order', 'adminViewModel', dan 'onBack')
        composable(
            route = "admin_order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0

            // Mencari objek order berdasarkan ID yang dikirim lewat navigasi
            val selectedOrder = orders.find { it.idOrders == orderId }

            if (selectedOrder != null) {
                AdminOrderDetailScreen(
                    order = selectedOrder,           // Mengirim objek OrderEntity
                    adminViewModel = adminViewModel, // Mengirim ViewModel
                    onBack = { navController.popBackStack() } // Fungsi kembali
                )
            }
        }

        // RUTE MANAJEMEN LAINNYA
        composable("manage_portfolio") { ManagePortfolioScreen() }
        composable("manage_service") { ManageServiceScreen() }
        composable("manage_order") { ManageOrderScreen() }
    }
}
