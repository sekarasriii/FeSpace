package com.example.fespace.repository

import com.example.fespace.data.local.dao.OrderDocumentDao
import kotlinx.coroutines.flow.Flow
import com.example.fespace.data.local.entity.OrderDocumentEntity

class OrderDocumentRepository(
    private val orderDocumentDao: OrderDocumentDao
) {

    fun getDocumentsByOrder(orderId: Int): Flow<List<OrderDocumentEntity>> {
        return orderDocumentDao.getDocumentsByOrder(orderId)
    }

    suspend fun insert(document: OrderDocumentEntity) {
        orderDocumentDao.insert(document)
    }

    suspend fun delete(document: OrderDocumentEntity) {
        orderDocumentDao.delete(document)
    }
}
