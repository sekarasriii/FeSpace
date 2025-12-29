package com.example.fespace.repository

import com.example.fespace.data.local.dao.UserDao
import com.example.fespace.data.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun getUser(email: String, pass: String): UserEntity? {
        return userDao.getUser(email, pass)
    }

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.update(user)
    }

    suspend fun deleteUser(user: UserEntity) {
        userDao.delete(user)
    }
}
