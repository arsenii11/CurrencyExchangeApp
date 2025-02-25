// File: com/example/currencyExchangeApplication/di/AppModule.kt

package com.example.currencyExchangeApplication.domain.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.currencyExchangeApplication.data.database.AppDatabase
import com.example.currencyExchangeApplication.data.database.dao.ConversionHistoryDao
import com.example.currencyExchangeApplication.data.database.dao.ConversionRateDao
import com.example.currencyExchangeApplication.data.database.dao.QuickAccessPairsDao
import com.example.currencyExchangeApplication.data.database.dao.UserDao
import com.example.currencyExchangeApplication.data.database.dao.UserPreferencesDao
import com.example.currencyExchangeApplication.data.repository.HomeRepository
import com.example.currencyExchangeApplication.data.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAppContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration() // Recreates the database on version changes
            .build()
    }

    /**
     * Provides ConversionHistoryDao.
     */
    @Provides
    @Singleton
    fun provideConversionHistoryDao(db: AppDatabase): ConversionHistoryDao {
        return db.conversionHistoryDao()
    }

    /**
     * Provides ConversionRateDao.
     */
    @Provides
    @Singleton
    fun provideConversionRateDao(db: AppDatabase): ConversionRateDao {
        return db.conversionRateDao()
    }

    /**
     * Provides UserDao.
     */
    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    /**
     * Provides UserPreferencesDao.
     */
    @Provides
    @Singleton
    fun provideUserPreferencesDao(appDatabase: AppDatabase): UserPreferencesDao {
        return appDatabase.userPreferencesDao()
    }

    /**
     * Provides QuickAccessPairsDao.
     */
    @Provides
    @Singleton
    fun provideQuickAccessPairsDao(appDatabase: AppDatabase): QuickAccessPairsDao {
        return appDatabase.quickAccessPairsDao()
    }

    /**
     * Provides HomeRepository.
     */
    @Provides
    @Singleton
    fun provideHomeRepository(userDao: UserDao): HomeRepository {
        return HomeRepository(userDao)
    }

    /**
     * Provides UserPreferencesRepository.
     */
    @Provides
    @Singleton
    fun provideUserPreferencesRepository(userPreferencesDao: UserPreferencesDao): UserPreferencesRepository {
        return UserPreferencesRepository(userPreferencesDao)
    }
}
