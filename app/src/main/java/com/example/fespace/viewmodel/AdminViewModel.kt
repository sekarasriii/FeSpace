package com.example.fespace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fespace.data.local.entity.PortfolioEntity
import com.example.fespace.data.local.entity.ServiceEntity
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.repository.OrderRepository
import com.example.fespace.repository.PortfolioRepository
import com.example.fespace.repository.ServiceRepository
import com.example.fespace.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val portfolioRepository: PortfolioRepository,
    private val serviceRepository: ServiceRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // --- STATE DATA (Menggunakan StateFlow sebagai Single Source of Truth) ---
    // Kita menggunakan .stateIn agar Flow dari Room selalu aktif selama ViewModel hidup

    val services: StateFlow<List<ServiceEntity>> = serviceRepository.getAllServices()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val portfolios: StateFlow<List<PortfolioEntity>> = portfolioRepository.getAllPortfolio()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val orders: StateFlow<List<OrderEntity>> = orderRepository.getAllOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )


    // --- CRUD SERVICES ---

    fun addService(name: String, desc: String, price: Double, duration: String, features: String, adminId: Int) {
        viewModelScope.launch {
            serviceRepository.addService(
                ServiceEntity(
                    idAdmin = adminId,
                    nameServices = name,
                    description = desc,
                    priceStart = price,
                    durationEstimate = duration,
                    features = features
                )
            )
        }
    }

    fun updateService(service: ServiceEntity) {
        viewModelScope.launch {
            serviceRepository.updateService(service)
        }
    }

    fun deleteService(service: ServiceEntity) {
        viewModelScope.launch {
            serviceRepository.deleteService(service)
        }
    }


    // --- CRUD PORTFOLIO ---

    fun addPortfolio(title: String, desc: String, category: String, year: Int, adminId: Int) {
        viewModelScope.launch {
            portfolioRepository.insert(
                PortfolioEntity(
                    idAdmin = adminId,
                    title = title,
                    description = desc,
                    category = category,
                    year = year
                )
            )
        }
    }

    fun updatePortfolio(portfolio: PortfolioEntity) {
        viewModelScope.launch {
            portfolioRepository.update(portfolio)
        }
    }

    fun deletePortfolio(portfolio: PortfolioEntity) {
        viewModelScope.launch {
            portfolioRepository.delete(portfolio)
        }
    }


    // --- CRUD ORDERS ---

    fun updateOrderStatus(order: OrderEntity, newStatus: String) {
        viewModelScope.launch {
            // Kita copy order lama dan ganti statusnya saja
            val updatedOrder = order.copy(
                status = newStatus,
                updateAt = System.currentTimeMillis()
            )
            orderRepository.update(updatedOrder)
        }
    }

    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch {
            orderRepository.delete(order)
        }
    }
}
