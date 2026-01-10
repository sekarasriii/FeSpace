package com.example.fespace.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fespace.data.local.dao.ServiceDao
import com.example.fespace.data.local.dao.UserDao
import com.example.fespace.data.local.entity.UserEntity
import com.example.fespace.data.local.dao.PortfolioDao
import com.example.fespace.data.local.dao.OrderDao
import com.example.fespace.data.local.dao.OrderDocumentDao
import com.example.fespace.data.local.entity.ServiceEntity
import com.example.fespace.data.local.entity.PortfolioEntity
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.data.local.entity.OrderDocumentEntity


@Database(entities =
    [
        UserEntity::class,
        PortfolioEntity::class,
        ServiceEntity::class,
        OrderEntity::class,
        OrderDocumentEntity::class
    ],
    version = 7, exportSchema = false)
abstract class FeSpaceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun serviceDao(): ServiceDao
    abstract fun orderDao(): OrderDao
    abstract fun orderDocumentDao(): OrderDocumentDao

    companion object {
        @Volatile
        private var INSTANCE: FeSpaceDatabase? = null

        fun getInstance(context: Context): FeSpaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FeSpaceDatabase::class.java,
                    "fespace_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
