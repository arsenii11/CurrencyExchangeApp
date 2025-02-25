package com.example.currencyExchangeApplication.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_conversions",
    foreignKeys = [androidx.room.ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )]
)
data class FavoriteConversionsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val fromCurrency: String,
    val toCurrency: String,
    val userNote: String? = null
)
