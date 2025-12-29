package com.example.fespace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fespace.repository.OrderRepository
import com.example.fespace.repository.PortfolioRepository
import com.example.fespace.repository.ServiceRepository
import com.example.fespace.repository.UserRepository

class ViewModelFactory(
    private val userRepo: UserRepository,
    private val portfolioRepo: PortfolioRepository,
    private val serviceRepo: ServiceRepository,
    private val orderRepo: OrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(userRepo) as T
            modelClass.isAssignableFrom(AdminViewModel::class.java) ->
                AdminViewModel(portfolioRepo, serviceRepo, orderRepo, userRepo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
