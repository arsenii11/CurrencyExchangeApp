package com.example.currencyExchangeApplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyExchangeApplication.data.database.entities.UserPreferencesEntity

@Dao
interface UserPreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePreferences(entity: UserPreferencesEntity)

    @Query("SELECT * FROM user_preferences WHERE userId = :userId LIMIT 1")
    suspend fun getUserPreferences(userId: Long): UserPreferencesEntity?

    @Query("DELETE FROM user_preferences WHERE userId = :userId")
    suspend fun deletePreferencesForUser(userId: Long)
}

