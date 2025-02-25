package com.example.currencyExchangeApplication.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_assignments",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = ConversionHistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversionId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        ),
        androidx.room.ForeignKey(
            entity = ConversionCategoriesEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class CategoryAssignmentsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversionId: Long, // Foreign key to `conversion_history`
    val categoryId: Long // Foreign key to `conversion_categories`
)
