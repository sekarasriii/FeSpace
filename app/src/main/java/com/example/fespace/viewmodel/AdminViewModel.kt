package com.example.fespace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fespace.data.local.entity.PortfolioEntity
import com.example.fespace.data.local.entity.ServiceEntity
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.repository.OrderRepository
import com.example.fespace.repository.PortfolioRepository
import com.example.fespace.repository.ServiceRepository
import com.example.fespace.repository.UserRepository
import com.example.fespace.repository.OrderDocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.text.contains
import kotlinx.coroutines.flow.combine

class AdminViewModel(
    private val portfolioRepository: PortfolioRepository,
    private val serviceRepository: ServiceRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val orderDocumentRepository: OrderDocumentRepository
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

    // Masukkan di dalam class AdminViewModel
    val clientCount: StateFlow<Int> = userRepository.getClientCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0
        )

    val clients: StateFlow<List<com.example.fespace.data.local.entity.UserEntity>> = userRepository.getAllClients()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    // State untuk Filter
    var filterStatus = mutableStateOf<String?>(null)
    var filterClientName = mutableStateOf<String?>(null)
    var filterStartDate = mutableStateOf<Long?>(null)
    var filterEndDate = mutableStateOf<Long?>(null)

    // Mengambil data orders berdasarkan filter
    val FilteredOrders: StateFlow<List<OrderEntity>> = combine(
        orderRepository.getAllOrders(),
        snapshotFlow { filterStatus.value },
        snapshotFlow { filterClientName.value },
        snapshotFlow { filterStartDate.value },
        snapshotFlow { filterEndDate.value }
    ) { allOrders, status, clientName, startDate, endDate ->
        allOrders.filter { order ->
            // Filter by status
            val statusMatch = status == null || order.status == status
            
            // Filter by date range
            val dateMatch = (startDate == null || order.createAt >= startDate) &&
                           (endDate == null || order.createAt <= endDate)
            
            statusMatch && dateMatch
        }.filter { order ->
            // Filter by client name (synchronous check using runBlocking for simplicity)
            if (clientName.isNullOrBlank()) {
                true
            } else {
                kotlinx.coroutines.runBlocking {
                    val client = userRepository.getUserById(order.idClient)
                    client?.nameUser?.contains(clientName, ignoreCase = true) == true
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- CRUD SERVICES ---

    fun addService(name: String, category: String, desc: String, price: Double, duration: String, features: String, imagePath: String?, adminId: Int) {
        viewModelScope.launch {
            serviceRepository.addService(
                ServiceEntity(nameServices = name,
                    category = category.lowercase().trim(), // Simpan sebagai huruf kecil & rapi
                    description = desc,
                    priceStart = price,
                    durationEstimate = duration,
                    features = features,
                    imagePath = imagePath,
                    idAdmin = adminId
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

    fun addPortfolio(title: String, desc: String, category: String, year: Int, imagePath: String?, adminId: Int) {
        viewModelScope.launch {
            portfolioRepository.insert(
                PortfolioEntity(
                    idAdmin = adminId,
                    title = title,
                    description = desc,
                    category = category,
                    year = year,
                    imagePath = imagePath // TAMBAHKAN INI
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

    fun getOrderById(orderId: Int): Flow<OrderEntity?> {
        return orderRepository.getOrderByIdFlow(orderId)
    }

    fun updateOrderStatus(order: OrderEntity, newStatus: String) {
        viewModelScope.launch {
            val updatedOrder = order.copy(
                status = newStatus,
                updateAt = System.currentTimeMillis()
            )
            orderRepository.update(updatedOrder)
        }
    }

    fun updateOrderDesign(order: OrderEntity, designPath: String, adminId: Int) {
        viewModelScope.launch {
            // Update order entity with design path
            val updatedOrder = order.copy(
                designPath = designPath,
                updateAt = System.currentTimeMillis()
            )
            orderRepository.update(updatedOrder)
            
            // Also save to order_documents table as per SRS
            val fileName = designPath.substringAfterLast("/")
            orderDocumentRepository.uploadDocument(
                orderId = order.idOrders,
                uploadedBy = adminId,
                filePath = designPath,
                fileName = fileName,
                docType = "design_draft",
                description = "Hasil desain dari admin untuk order #${order.idOrders}"
            )
        }
    }

    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch {
            orderRepository.delete(order)
        }
    }

    // --- USER ---
    fun getClientById(clientId: Int): Flow<com.example.fespace.data.local.entity.UserEntity?> {
        return userRepository.getUserByIdFlow(clientId)
    }

    val adminProfile: Flow<com.example.fespace.data.local.entity.UserEntity?> = 
        userRepository.getUserByEmailFlow("rahayu@gmail.com")

    fun updateAdminProfile(user: com.example.fespace.data.local.entity.UserEntity) {
        viewModelScope.launch {
            userRepository.update(user)
        }
    }
    
    // --- DOCUMENT MANAGEMENT ---
    
    /**
     * Get all documents for an order
     */
    fun getOrderDocuments(orderId: Int): Flow<List<com.example.fespace.data.local.entity.OrderDocumentEntity>> {
        return orderDocumentRepository.getDocumentsByOrder(orderId)
    }
    
    /**
     * Get documents by type
     */
    fun getOrderDocumentsByType(orderId: Int, docType: String): Flow<List<com.example.fespace.data.local.entity.OrderDocumentEntity>> {
        return orderDocumentRepository.getDocumentsByType(orderId, docType)
    }
    
    /**
     * Upload a new document
     */
    fun uploadDocument(
        orderId: Int,
        uploadedBy: Int,
        filePath: String,
        fileName: String,
        docType: String,
        description: String? = null
    ) {
        viewModelScope.launch {
            orderDocumentRepository.uploadDocument(
                orderId = orderId,
                uploadedBy = uploadedBy,
                filePath = filePath,
                fileName = fileName,
                docType = docType,
                description = description
            )
        }
    }
    
    /**
     * Delete a document
     */
    fun deleteDocument(document: com.example.fespace.data.local.entity.OrderDocumentEntity) {
        viewModelScope.launch {
            orderDocumentRepository.delete(document)
        }
    }
}
