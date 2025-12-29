package com.example.fespace.view.navigation

sealed class Screen(val route: String) {

    object Welcome : Screen("welcome")

    object Login : Screen("login")
    object Register : Screen("register")

    object ClientHome : Screen("client_home")
    object OrderService : Screen("order_service")
    object OrderStatus : Screen("order_status")

    object AdminDashboard : Screen("admin_dashboard")
    object ManagePortfolio : Screen("manage_portfolio")
    object ManageService : Screen("manage_service")
    object ManageOrder : Screen("manage_order")
}
