package com.example.fespace.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fespace.data.local.database.FeSpaceDatabase
import com.example.fespace.repository.UserRepository
import com.example.fespace.view.auth.LoginScreen
import com.example.fespace.view.auth.RegisterScreen
import com.example.fespace.view.client.ClientHomeScreen
import com.example.fespace.view.client.OrderServiceScreen
import com.example.fespace.view.client.OrderStatusScreen
import com.example.fespace.view.common.WelcomeScreen
import com.example.fespace.viewmodel.AuthViewModel
import com.example.fespace.viewmodel.ViewModelFactory
import com.example.fespace.repository.OrderRepository
import com.example.fespace.repository.PortfolioRepository
import com.example.fespace.repository.ServiceRepository
import com.example.fespace.view.admin.AdminDashboardScreen
import com.example.fespace.view.admin.AdminRoute
import com.example.fespace.viewmodel.AdminViewModel

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Initialize Database
    val database = FeSpaceDatabase.getInstance(context)

    // 2. Initialize Daos
    val userDao = database.userDao()
    val orderDao = database.orderDao()
    val portfolioDao = database.portfolioDao()
    val serviceDao = database.serviceDao()

    // 3. Initialize Repositories
    val userRepository = UserRepository(userDao)
    val orderRepository = OrderRepository(orderDao)
    val portfolioRepository = PortfolioRepository(portfolioDao)
    val serviceRepository = ServiceRepository(serviceDao)

    // 4. Initialize ViewModel
    val authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory(
            userRepo = userRepository,
            orderRepo = orderRepository,
            portfolioRepo = portfolioRepository,
            serviceRepo = serviceRepository
        )
    )

    //update versi 2
    val adminViewModel: AdminViewModel = viewModel(
        factory = ViewModelFactory(
            userRepo = userRepository,
            orderRepo = orderRepository,
            portfolioRepo = portfolioRepository,
            serviceRepo = serviceRepository
        )
    )

    NavHost(navController = navController, startDestination = startDestination) {

        // WELCOME SCREEN
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        // LOGIN SCREEN
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                onLoginSuccess = { role ->
                    // Gunakan Log untuk melihat apakah role benar-benar masuk
                    android.util.Log.d("NAV_DEBUG", "User Role: $role")

                    // Pastikan route ini TERDAFTAR di bawah (di NavHost ini)
                    if (role.equals("admin", ignoreCase = true)) {
                        navController.navigate("admin_route") {
                            // Menghapus layar login dari stack agar tidak bisa "back" ke login
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate("client_route") {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onBack = { navController.popBackStack() }
            )
        }

        // REGISTER
        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // CLIENT DASHBOARD (Rute ini harus sama dengan yang di-navigate di atas)
        composable("client_route") {
            ClientHomeScreen(onOrderClick = { /* navigasi order */ })
        }

        // ADMIN DASHBOARD (Rute ini harus sama dengan yang di-navigate di atas)
        composable("admin_route") {
            AdminDashboardScreen(adminViewModel = adminViewModel)
        }

        composable("order_service") {
            OrderServiceScreen(
                onOrderSuccess = { navController.navigate("order_status") }
            )
        }


        composable("order_status") {
            OrderStatusScreen()
        }
    }
}
