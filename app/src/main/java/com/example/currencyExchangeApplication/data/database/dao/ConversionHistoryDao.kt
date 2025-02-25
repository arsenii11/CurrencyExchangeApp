package com.example.currencyExchangeApplication.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.presentation.history.HistoryListItem

@Dao
interface ConversionHistoryDao {

    @Query("""
    SELECT 
        conversion_history.id, 
        conversion_history.fromCurrency, 
        conversion_history.toCurrency, 
        conversion_history.amount, 
        conversion_history.convertedValue, 
        COALESCE(conversion_rate_record.rateAtConversion, 'N/A') AS rateAtConversion 
    FROM conversion_history
    LEFT JOIN conversion_rate_record ON conversion_history.id = conversion_rate_record.conversionId
""")
    fun getHistoryWithRates(): LiveData<List<HistoryListItem>>

    @Query("SELECT * FROM conversion_history")
    fun getLatestTransactions(): LiveData<List<ConversionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: ConversionHistoryEntity): Long

    @Query("DELETE FROM conversion_history")
    suspend fun deleteAllTransactions()
}
