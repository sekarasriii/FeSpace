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
    
    /**
     * Get documents by order and type
     * @param docType: 'location_photo', 'client_document', 'design_draft', 'final_design', 'other'
     */
    fun getDocumentsByType(orderId: Int, docType: String): Flow<List<OrderDocumentEntity>> {
        return orderDocumentDao.getDocumentsByOrderAndType(orderId, docType)
    }

    suspend fun insert(document: OrderDocumentEntity) {
        orderDocumentDao.insert(document)
    }

    suspend fun delete(document: OrderDocumentEntity) {
        orderDocumentDao.delete(document)
    }
    
    /**
     * Helper: Upload a new document
     */
    suspend fun uploadDocument(
        orderId: Int,
        uploadedBy: Int,
        filePath: String,
        fileName: String,
        docType: String,
        description: String? = null
    ) {
        android.util.Log.d("OrderDocRepo", "Uploading document: orderId=$orderId, fileName=$fileName, docType=$docType")
        val document = OrderDocumentEntity(
            idOrders = orderId,
            uploadedBy = uploadedBy,
            filePath = filePath,
            fileName = fileName,
            docType = docType,
            description = description
        )
        insert(document)
        android.util.Log.d("OrderDocRepo", "Document saved successfully: $fileName")
    }
}
