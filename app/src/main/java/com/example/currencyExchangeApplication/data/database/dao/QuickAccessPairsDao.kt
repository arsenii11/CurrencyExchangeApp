package com.example.currencyExchangeApplication.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyExchangeApplication.data.database.entities.QuickAccessPairsEntity

@Dao
interface QuickAccessPairsDao {
    @Query("SELECT * FROM quick_access_pairs WHERE userId = :userId ORDER BY usageCount DESC")
    suspend fun getQuickAccessPairs(userId: Long): List<QuickAccessPairsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuickAccessPair(pair: QuickAccessPairsEntity)

    @Query("UPDATE quick_access_pairs SET usageCount = usageCount + 1 WHERE id = :pairId")
    suspend fun incrementUsageCount(pairId: Long)

    @Delete
    suspend fun deleteQuickAccessPair(pair: QuickAccessPairsEntity)
}