package com.example.currencyExchangeApplication.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "conversion_rate_record",
    foreignKeys = [
        ForeignKey(
            entity = ConversionHistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversionId"],
            onDelete = ForeignKey.CASCADE // Delete rate record if the associated conversion is deleted
        )
    ]
)
data class ConversionRateRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val rateRecordId: Long = 0,
    val conversionId: Long,  // Foreign key linking to ConversionHistoryEntity
    val rateAtConversion: String, // Exchange rate at the time of conversion (e.g., "1.2")
    val timestamp: Long  // Time when the conversion was performed
)
