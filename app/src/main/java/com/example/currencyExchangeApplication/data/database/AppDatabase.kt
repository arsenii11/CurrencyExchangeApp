package com.example.currencyExchangeApplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyExchangeApplication.data.database.dao.ConversionHistoryDao
import com.example.currencyExchangeApplication.data.database.dao.ConversionRateDao
import com.example.currencyExchangeApplication.data.database.dao.QuickAccessPairsDao
import com.example.currencyExchangeApplication.data.database.dao.UserDao
import com.example.currencyExchangeApplication.data.database.dao.UserPreferencesDao
import com.example.currencyExchangeApplication.data.database.entities.AppLogsEntity
import com.example.currencyExchangeApplication.data.database.entities.CategoryAssignmentsEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionCategoriesEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionRateRecordEntity
import com.example.currencyExchangeApplication.data.database.entities.ExchangeSessionEntity
import com.example.currencyExchangeApplication.data.database.entities.FavoriteConversionsEntity
import com.example.currencyExchangeApplication.data.database.entities.QuickAccessPairsEntity
import com.example.currencyExchangeApplication.data.database.entities.UserEntity
import com.example.currencyExchangeApplication.data.database.entities.UserPreferencesEntity

@Database(
    entities = [
        ConversionHistoryEntity::class,
        ConversionRateRecordEntity::class,
        UserEntity::class,
        UserPreferencesEntity::class,
        FavoriteConversionsEntity::class,
        ConversionCategoriesEntity::class,
        CategoryAssignmentsEntity::class,
        AppLogsEntity::class,
        ExchangeSessionEntity::class,
        QuickAccessPairsEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun conversionHistoryDao(): ConversionHistoryDao
    abstract fun conversionRateDao(): ConversionRateDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    abstract fun userDao(): UserDao
    abstract fun quickAccessPairsDao(): QuickAccessPairsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Пересоздает базу данных при увеличении версии
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
