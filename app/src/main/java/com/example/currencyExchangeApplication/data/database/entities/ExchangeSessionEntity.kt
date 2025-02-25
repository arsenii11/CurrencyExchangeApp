package com.example.currencyExchangeApplication.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_sessions")
data class ExchangeSessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val startTime: Long,  // Время начала сессии (timestamp)
    val endTime: Long?,   // Время окончания сессии (timestamp, может быть null)
    val conversionCount: Int = 0 // Количество конверсий за сессию
)
