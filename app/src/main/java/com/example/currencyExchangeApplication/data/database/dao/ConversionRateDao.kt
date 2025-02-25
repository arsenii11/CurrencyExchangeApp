package com.example.currencyExchangeApplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyExchangeApplication.data.database.entities.ConversionRateRecordEntity

@Dao
interface ConversionRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRateRecord(rateRecord: ConversionRateRecordEntity)

    @Query("SELECT * FROM conversion_rate_record WHERE conversionId = :conversionId")
    suspend fun getRateByConversionId(conversionId: Long): ConversionRateRecordEntity?
}
