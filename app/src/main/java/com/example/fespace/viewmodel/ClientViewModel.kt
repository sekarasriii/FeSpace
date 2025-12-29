package com.example.fespace.viewmodel

import com.example.fespace.repository.PortfolioRepository
import com.example.fespace.repository.ServiceRepository
import com.example.fespace.repository.OrderRepository
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.data.local.entity.PortfolioEntity
import com.example.fespace.data.local.entity.ServiceEntity


class ClientViewModel(
    private val portfolioRepository: PortfolioRepository,
    private val serviceRepository: ServiceRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val portfolioList: StateFlow<List<PortfolioEntity>> =
        portfolioRepository.getAllPortfolio()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val serviceList: StateFlow<List<ServiceEntity>> =
        serviceRepository.getAllServices()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getOrdersByClient(clientId: Int): StateFlow<List<OrderEntity>> {
        return orderRepository.getOrdersByClient(clientId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun createOrder(order: OrderEntity) {
        viewModelScope.launch {
            orderRepository.insert(order)
        }
    }
}