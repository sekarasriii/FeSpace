package com.example.fespace.repository

import com.example.fespace.data.local.dao.ServiceDao
import com.example.fespace.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

class ServiceRepository(
    private val serviceDao: ServiceDao
) {
    suspend fun addService(service: ServiceEntity) = serviceDao.insertService(service)
    suspend fun updateService(service: ServiceEntity) = serviceDao.updateService(service)
    suspend fun deleteService(service: ServiceEntity) = serviceDao.deleteService(service)

    // Gunakan fungsi ini sebagai sumber data utama
    fun getAllServices(): Flow<List<ServiceEntity>> {
        return serviceDao.getAllServices()
    }

    fun getServicesByAdmin(adminId: Int): Flow<List<ServiceEntity>> {
        return serviceDao.getServicesByAdmin(adminId)
    }

    suspend fun getServiceById(id: Int): ServiceEntity? {
        return serviceDao.getServiceById(id)
    }

    // Fungsi-fungsi alias di bawah ini bisa dihapus jika ingin lebih rapi,
    // karena sudah diwakili oleh addService, updateService, dan deleteService di atas.
    suspend fun insert(service: ServiceEntity) {
        serviceDao.insertService(service)
    }

    suspend fun update(service: ServiceEntity) {
        serviceDao.updateService(service)
    }

    suspend fun delete(service: ServiceEntity) {
        serviceDao.deleteService(service)
    }
}
