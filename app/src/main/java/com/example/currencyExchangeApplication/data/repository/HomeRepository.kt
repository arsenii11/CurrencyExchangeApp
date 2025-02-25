// File: com/example/currencyExchangeApplication/data/repository/HomeRepository.kt

package com.example.currencyExchangeApplication.data.repository

import com.example.currencyExchangeApplication.data.database.dao.UserDao
import com.example.currencyExchangeApplication.data.database.entities.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val userDao: UserDao
) {
    /**
     * Saves or updates a user in the 'users' table.
     * Assumes single-user scenario with userId=1.
     */
    suspend fun saveUser(userName: String, email: String = "") {
        val user = UserEntity(
            userName = userName,
        )
        userDao.insertOrUpdateUser(user)
    }

    /**
     * Fetches the user by userId.
     * Assumes single-user scenario with userId=1.
     */
    suspend fun fetchUser(): UserEntity? {
        return userDao.getUserById(1L)
    }

    /**
     * Deletes the user by userId.
     */
    suspend fun deleteUser() {
        userDao.deleteUser(1L)
    }
}
