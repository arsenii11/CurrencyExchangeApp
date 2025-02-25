package com.example.currencyExchangeApplication.presentation

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyExchangeApplication.presentation.exchangescreen.ExchangeScreen
import com.example.currencyExchangeApplication.presentation.history.HistoryScreen
import com.example.currencyExchangeApplication.presentation.homescreen.HomeScreen
import com.example.currencyExchangeApplication.presentation.settings.SettingsScreen
import com.example.currencyExchangeApplication.presentation.settings.SettingsScreenActions
import com.example.currencyExchangeApplication.presentation.settings.SettingsViewModel
import com.utilities.MyReceiver
import dagger.hilt.android.AndroidEntryPoint

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.currencyExchangeApplication.presentation.composeStyles.CurrencyExchangeApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val receiver = MyReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        // MainActivity.kt
        setContent {
            // Получаем экземпляр SettingsViewModel, привязанный к Activity
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.settingsState.collectAsState()

            CurrencyExchangeApplicationTheme(
                darkTheme = settingsState.theme.lowercase() == "dark"
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController, settingsViewModel = settingsViewModel)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Exchange : Screen("exchange")
    object History : Screen("history")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController,settingsViewModel: SettingsViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartClick = { navController.navigate(Screen.Exchange.route) }
            )
        }

        composable(Screen.Exchange.route) {
            ExchangeScreen(
                navController = navController,
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                navController = navController
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = settingsViewModel
            )
        }
    }
}

