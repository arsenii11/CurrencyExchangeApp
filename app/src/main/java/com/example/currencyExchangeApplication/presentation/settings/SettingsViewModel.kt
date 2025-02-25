// File: com/example/currencyExchangeApplication/presentation/settings/SettingsViewModel.kt

package com.example.currencyExchangeApplication.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.database.entities.UserPreferencesEntity
import com.example.currencyExchangeApplication.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsScreenState())
    val settingsState: StateFlow<SettingsScreenState> = _settingsState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        Log.d("SettingsViewModel", "Initializing ViewModel")
        loadUserPreferences()
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            try {
                Log.d("SettingsViewModel", "Starting to load preferences")
                val preferences = userPreferencesRepository.getUserPreferences(1L)
                Log.d("SettingsViewModel", "Loaded preferences: $preferences")

                if (preferences != null) {
                    _settingsState.update { currentState ->
                        currentState.copy(
                            defaultFromCurrency = preferences.preferredFromCurrency,
                            defaultToCurrency = preferences.preferredToCurrency,
                            theme = preferences.theme.lowercase() // Ensure case matching
                        ).also {
                            Log.d("SettingsViewModel", "Updated state: $it")
                        }
                    }
                } else {
                    Log.d("SettingsViewModel", "No preferences found for user")
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error loading preferences", e)
                _snackbarMessage.value = "Failed to load preferences: ${e.message}"
            }
        }
    }

    fun savePreferences(fromCurrency: String, toCurrency: String, theme: String) {
        viewModelScope.launch {
            try {
                if (fromCurrency == toCurrency) {
                    _snackbarMessage.value = "From and To currencies cannot be the same"
                    return@launch
                }

                val preferences = UserPreferencesEntity(
                    userId = 1L,
                    preferredFromCurrency = fromCurrency,
                    preferredToCurrency = toCurrency,
                    theme = theme
                )
                userPreferencesRepository.saveUserPreferences(preferences)

                // Обновляем состояние сразу
                _settingsState.value = _settingsState.value.copy(
                    defaultFromCurrency = fromCurrency,
                    defaultToCurrency = toCurrency,
                    theme = theme.lowercase()
                )
                _snackbarMessage.value = "Preferences saved successfully"
            } catch (e: Exception) {
                _snackbarMessage.value = "Failed to save preferences: ${e.message}"
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}