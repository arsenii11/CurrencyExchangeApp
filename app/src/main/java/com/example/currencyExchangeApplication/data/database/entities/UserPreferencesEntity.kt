package com.example.currencyExchangeApplication.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_preferences",
    foreignKeys = [androidx.room.ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )]
)
data class UserPreferencesEntity(
    @PrimaryKey val id: Long = 1,
    val userId: Long, // Foreign key
    val preferredFromCurrency: String,
    val preferredToCurrency: String,
    val theme: String // dark, light
)
