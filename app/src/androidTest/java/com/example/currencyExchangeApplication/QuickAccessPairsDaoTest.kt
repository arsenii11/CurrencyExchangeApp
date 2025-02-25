package com.example.currencyExchangeApplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencyExchangeApplication.data.database.AppDatabase
import com.example.currencyExchangeApplication.data.database.dao.QuickAccessPairsDao
import com.example.currencyExchangeApplication.data.database.entities.QuickAccessPairsEntity
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class QuickAccessPairsDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: QuickAccessPairsDao

    @Before
    fun createDb() {
        // Создание in-memory базы данных
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries() // Разрешение выполнения запросов в главном потоке для тестов
            .build()
        dao = db.quickAccessPairsDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertQuickAccessPairAndRetrieve() = runBlocking {
        // Arrange
        val pair = QuickAccessPairsEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L,
            usageCount = 0
        )

        // Act
        dao.insertQuickAccessPair(pair)
        val retrievedPairs = dao.getQuickAccessPairs(userId = 1L)

        // Assert
        assertEquals(1, retrievedPairs.size)
        assertEquals(pair.fromCurrency, retrievedPairs[0].fromCurrency)
        assertEquals(pair.toCurrency, retrievedPairs[0].toCurrency)
    }

    @Test
    fun getQuickAccessPairs_orderedByUsageCount() = runBlocking {
        // Arrange
        val pair1 = QuickAccessPairsEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L,
            usageCount = 5
        )
        val pair2 = QuickAccessPairsEntity(
            fromCurrency = "GBP",
            toCurrency = "JPY",
            userId = 1L,
            usageCount = 10
        )
        val pair3 = QuickAccessPairsEntity(
            fromCurrency = "AUD",
            toCurrency = "CAD",
            userId = 1L,
            usageCount = 3
        )

        // Act
        dao.insertQuickAccessPair(pair1)
        dao.insertQuickAccessPair(pair2)
        dao.insertQuickAccessPair(pair3)
        val retrievedPairs = dao.getQuickAccessPairs(userId = 1L)

        // Assert
        assertEquals(3, retrievedPairs.size)
        assertEquals(pair2, retrievedPairs[0]) // Highest usageCount
        assertEquals(pair1, retrievedPairs[1])
        assertEquals(pair3, retrievedPairs[2])
    }

    @Test
    fun incrementUsageCount_shouldIncreaseUsageCount() = runBlocking {
        // Arrange
        val pair = QuickAccessPairsEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L,
            usageCount = 0
        )
        dao.insertQuickAccessPair(pair)

        // Act
        dao.incrementUsageCount(pairId = pair.id)
        val updatedPair = dao.getQuickAccessPairs(userId = 1L).first()

        // Assert
        assertEquals(1, updatedPair.usageCount)
    }

    @Test
    fun deleteQuickAccessPair_shouldRemovePair() = runBlocking {
        // Arrange
        val pair = QuickAccessPairsEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L,
            usageCount = 0
        )
        dao.insertQuickAccessPair(pair)

        // Act
        dao.deleteQuickAccessPair(pair)
        val retrievedPairs = dao.getQuickAccessPairs(userId = 1L)

        // Assert
        assertTrue(retrievedPairs.isEmpty())
    }

    @Test
    fun insertQuickAccessPair_duplicate_shouldReplace() = runBlocking {
        // Arrange
        val pair1 = QuickAccessPairsEntity(
            id = 1L, // Предполагается, что id автоинкрементируется
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L,
            usageCount = 0
        )
        val pair2 = QuickAccessPairsEntity(
            id = 1L, // Дублирующий ID
            fromCurrency = "USD",
            toCurrency = "GBP",
            userId = 1L,
            usageCount = 1
        )

        // Act
        dao.insertQuickAccessPair(pair1)
        dao.insertQuickAccessPair(pair2) // Должен заменить pair1 из-за OnConflictStrategy.REPLACE
        val retrievedPairs = dao.getQuickAccessPairs(userId = 1L)

        // Assert
        assertEquals(1, retrievedPairs.size)
        assertEquals(pair2.toCurrency, retrievedPairs[0].toCurrency)
        assertEquals(pair2.usageCount, retrievedPairs[0].usageCount)
    }

    @Test
    fun getQuickAccessPairs_differentUsers_shouldFilterCorrectly() = runBlocking {
        // Arrange
        val pairUser1 = QuickAccessPairsEntity(
            fromCurrency = "USD",
            toCurrency = "EUR",
            userId = 1L,
            usageCount = 2
        )
        val pairUser2 = QuickAccessPairsEntity(
            fromCurrency = "GBP",
            toCurrency = "JPY",
            userId = 2L,
            usageCount = 5
        )

        // Act
        dao.insertQuickAccessPair(pairUser1)
        dao.insertQuickAccessPair(pairUser2)
        val retrievedPairsUser1 = dao.getQuickAccessPairs(userId = 1L)
        val retrievedPairsUser2 = dao.getQuickAccessPairs(userId = 2L)

        // Assert
        assertEquals(1, retrievedPairsUser1.size)
        assertEquals(pairUser1, retrievedPairsUser1[0])

        assertEquals(1, retrievedPairsUser2.size)
        assertEquals(pairUser2, retrievedPairsUser2[0])
    }
}
