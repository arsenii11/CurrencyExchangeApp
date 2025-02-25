package com.example.currencyExchangeApplication.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quick_access_pairs")
data class QuickAccessPairsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val userId: Long,
    val usageCount: Int = 0
)
