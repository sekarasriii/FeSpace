package com.example.fespace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.fespace.data.local.entity.ServiceEntity
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertService(service: ServiceEntity)

    //@Update
    //suspend fun updateService(service: ServiceEntity)

    //@Delete
    //suspend fun deleteService(service: ServiceEntity)

    //@Query("SELECT * FROM services")
    //fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE idAdmin = :adminId")
    fun getServicesByAdmin(adminId: Int): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE idServices = :id")
    suspend fun getServiceById(id: Int): ServiceEntity?

    //update versi2
    @Query("SELECT * FROM services ORDER BY createAt DESC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)
}