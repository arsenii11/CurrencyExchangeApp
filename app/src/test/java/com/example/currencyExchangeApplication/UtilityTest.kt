package com.example.currencyExchangeApplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.utilities.Utility
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UtilityTest {

    private lateinit var mockContext: Context
    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetworkCapabilities: NetworkCapabilities
    private lateinit var mockNetworkInfo: NetworkInfo

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        mockConnectivityManager = mockk(relaxed = true)
        mockNetworkCapabilities = mockk(relaxed = true)
        mockNetworkInfo = mockk(relaxed = true)

        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
    }

    @Test
    fun `isNetworkAvailable returns true for WIFI on API = Q`() {
        mockkStatic(Build.VERSION::class)
        every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.Q

        every { mockConnectivityManager.activeNetwork } returns mockk()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true

        val result = Utility.isNetworkAvailable(mockContext)
        assertTrue(result)
    }

    @Test
    fun `isNetworkAvailable returns true for CELLULAR on API = Q`() {
        mockkStatic(Build.VERSION::class)
        every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.Q

        every { mockConnectivityManager.activeNetwork } returns mockk()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val result = Utility.isNetworkAvailable(mockContext)
        assertTrue(result)
    }

    @Test
    fun `isNetworkAvailable returns false when no network on API = Q`() {
        mockkStatic(Build.VERSION::class)
        every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.Q

        every { mockConnectivityManager.activeNetwork } returns null
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns null

        val result = Utility.isNetworkAvailable(mockContext)
        assertFalse(result)
    }

    @Test
    fun `isNetworkAvailable returns true when connected on API  Q`() {
        mockkStatic(Build.VERSION::class)
        every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.P

        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnected } returns true

        val result = Utility.isNetworkAvailable(mockContext)
        assertTrue(result)
    }

    @Test
    fun `isNetworkAvailable returns false when not connected on API  Q`() {
        mockkStatic(Build.VERSION::class)
        every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.P

        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnected } returns false

        val result = Utility.isNetworkAvailable(mockContext)
        assertFalse(result)
    }

    @Test
    fun `isNetworkAvailable returns false when context is null`() {
        val result = Utility.isNetworkAvailable(null)
        assertFalse(result)
    }
}
