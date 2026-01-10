package com.example.fespace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fespace.data.local.entity.UserEntity
import com.example.fespace.repository.UserRepository
import com.example.fespace.repository.OrderRepository
import com.example.fespace.repository.PortfolioRepository
import com.example.fespace.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isLoginValid(email: String, pass: String): Boolean {
        return email.isNotEmpty() && pass.isNotEmpty() && isValidEmail(email)
    }

    fun isRegisterValid(name: String, email: String, pass: String, whatsapp: String): Boolean {
        return name.isNotBlank() && email.contains("@") && pass.length >= 6 && whatsapp.isNotBlank()
    }

    fun register(name: String, email: String, pass: String, role: String, whatsapp: String, onResult: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newUser = UserEntity(
                    nameUser = name,
                    email = email,
                    password = pass,
                    role = role,
                    whatsappNumber = whatsapp
                )
                userRepository.insertUser(newUser) // Changed from userRepository to repository
                withContext(Dispatchers.Main) { onResult() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("DEBUG_REGISTER: Gagal daftar - ${e.message}")
                    onResult() // Panggil agar isLoading = false
                }
            }
        }
    }

    fun login(email: String, pass: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.login(email, pass)
            withContext(Dispatchers.Main) { onResult(user) }
        }
    }
    // Factory (Tetap di bawah)
    class ViewModelFactory(
        private val userRepo: UserRepository,private val portfolioRepo: PortfolioRepository,
        private val serviceRepo: ServiceRepository,
        private val orderRepo: OrderRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return when {
                modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                    AuthViewModel(userRepo) as T

                modelClass.isAssignableFrom(AdminViewModel::class.java) ->
                    AdminViewModel(
                        portfolioRepository = portfolioRepo,
                        serviceRepository = serviceRepo,
                        orderRepository = orderRepo,
                        userRepository = userRepo
                    ) as T

                modelClass.isAssignableFrom(ClientViewModel::class.java) ->
                    ClientViewModel(
                        orderRepository = orderRepo,
                        userRepository = userRepo,      // Tambahkan ini jika dibutuhkan
                        serviceRepository = serviceRepo,
                        portfolioRepository = portfolioRepo
                    ) as T

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

    }
}
