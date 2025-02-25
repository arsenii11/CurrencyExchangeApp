package com.example.currencyExchangeApplication.presentation.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.utilities.CurrenciesAvailable

/**
 * Holds UI-related data for the Settings screen.
 */
data class SettingsScreenState(
    val defaultFromCurrency: String = "EUR",
    val defaultToCurrency: String = "USD",
    // Используем значение в нижнем регистре для корректного сравнения
    val theme: String = "light"
)

/**
 * Defines callbacks for user interactions on the Settings screen.
 */
data class SettingsScreenActions(
    val onBackClick: () -> Unit,
    val onApplyClick: (fromCurrency: String, toCurrency: String, theme: String) -> Unit
)

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.settingsState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    var fromCurrency by remember(state.defaultFromCurrency) {
        mutableStateOf(state.defaultFromCurrency)
    }
    var toCurrency by remember(state.defaultToCurrency) {
        mutableStateOf(state.defaultToCurrency)
    }
    var theme by remember(state.theme) {
        mutableStateOf(state.theme)
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(message = it)
            viewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(Unit) {
        Log.d("SettingsScreen", "Initial state: $state")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CurrencyDropdown(
                label = "Default From Currency",
                selectedCurrency = fromCurrency,
                onCurrencySelected = { fromCurrency = it }
            )

            CurrencyDropdown(
                label = "Default To Currency",
                selectedCurrency = toCurrency,
                onCurrencySelected = { toCurrency = it }
            )

            ThemeSelector(
                currentTheme = theme,
                onThemeSelected = { theme = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Передаём значение темы в нижнем регистре для корректного сравнения
                    viewModel.savePreferences(fromCurrency, toCurrency, theme.lowercase())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save Changes")
            }

            // Debug info
            Text(
                text = "Current state: ${state}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CurrencyDropdown(
    label: String,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = CurrenciesAvailable.currenciesList()

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedCurrency)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                ) {
                    Text(currency)
                }
            }
        }
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: String,
    onThemeSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Theme",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeOption(
                text = "Light",
                selected = currentTheme == "light",
                onClick = { onThemeSelected("light") },
                modifier = Modifier.weight(1f)
            )
            ThemeOption(
                text = "Dark",
                selected = currentTheme == "dark",
                onClick = { onThemeSelected("dark") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (selected)
                MaterialTheme.colors.primary.copy(alpha = 0.1f)
            else
                MaterialTheme.colors.surface
        )
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val state = SettingsScreenState(
        defaultFromCurrency = "EUR",
        defaultToCurrency = "USD",
        theme = "dark" // для превью используем "dark" в нижнем регистре
    )
    val actions = SettingsScreenActions(
        onBackClick = {},
        onApplyClick = { fromCur, toCur, theme ->
            // Apply changes in your ViewModel or repository
        }
    )
}
