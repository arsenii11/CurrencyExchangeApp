package com.example.currencyExchangeApplication.data.repository

import com.example.currencyExchangeApplication.data.database.dao.UserPreferencesDao
import com.example.currencyExchangeApplication.data.database.entities.UserPreferencesEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDao: UserPreferencesDao
) {

    suspend fun saveUserPreferences(entity: UserPreferencesEntity) {
        userPreferencesDao.insertOrUpdatePreferences(entity)
    }

    suspend fun getUserPreferences(userId: Long): UserPreferencesEntity? {
        return userPreferencesDao.getUserPreferences(userId)
    }

    suspend fun deleteUserPreferences(userId: Long) {
        userPreferencesDao.deletePreferencesForUser(userId)
    }
}
