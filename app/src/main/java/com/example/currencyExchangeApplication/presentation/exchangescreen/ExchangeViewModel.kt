package com.example.currencyExchangeApplication.presentation.exchangescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.database.entities.ConversionRateRecordEntity
import com.example.currencyExchangeApplication.data.database.entities.QuickAccessPairsEntity
import com.example.currencyExchangeApplication.data.database.entities.UserPreferencesEntity
import com.example.currencyExchangeApplication.data.model.ExchangeScreenState
import com.example.currencyExchangeApplication.data.model.CurrencyState
import com.example.currencyExchangeApplication.data.repository.ExchangeRepository
import com.example.currencyExchangeApplication.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val defaultFromCurrency = "USD"
    private val defaultToCurrency = "EUR"

    private val _quickAccessPairs = MutableStateFlow<List<QuickAccessPairsEntity>>(emptyList())
    val quickAccessPairs: StateFlow<List<QuickAccessPairsEntity>> = _quickAccessPairs


    private val _exchangeScreenState = MutableStateFlow(ExchangeScreenState())
    val exchangeScreenState: StateFlow<ExchangeScreenState> = _exchangeScreenState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        viewModelScope.launch {
            val preferences = userPreferencesRepository.getUserPreferences(1L)
            if (preferences == null) {
                setDefaultExchangeScreenState(defaultFromCurrency, defaultToCurrency)
            } else {
                // Use existing preferences
                setDefaultExchangeScreenState(
                    preferences.preferredFromCurrency,
                    preferences.preferredToCurrency
                )
            }
            loadQuickAccessPairs()
        }
    }

    private fun setDefaultExchangeScreenState(fromCurrency: String, toCurrency: String) {
        _exchangeScreenState.value = ExchangeScreenState(
            currency1 = CurrencyState(
                selectedCurrency = fromCurrency,
                amount = "",
                onCurrencyChange = ::onCurrency1Changed,
                onAmountChange = ::onAmount1Changed
            ),
            currency2 = CurrencyState(
                selectedCurrency = toCurrency,
                amount = "",
                onCurrencyChange = ::onCurrency2Changed,
                onAmountChange = ::onAmount2Changed
            ),
            isLoading = false
        )
    }


    fun onAmount1Changed(newAmount: String) {
        _exchangeScreenState.update { currentState ->
            currentState.copy(
                currency1 = currentState.currency1.copy(
                    amount = newAmount
                )
            )
        }
    }

    fun onAmount2Changed(newAmount: String) {
        _exchangeScreenState.update { currentState ->
            currentState.copy(
                currency2 = currentState.currency2.copy(
                    amount = newAmount
                )
            )
        }
    }

    fun onCurrency1Changed(newCurrency: String) {
        _exchangeScreenState.update { currentState ->
            currentState.copy(
                currency1 = currentState.currency1.copy(
                    selectedCurrency = newCurrency
                )
            )
        }
    }

    fun onCurrency2Changed(newCurrency: String) {
        _exchangeScreenState.update { currentState ->
            currentState.copy(
                currency2 = currentState.currency2.copy(
                    selectedCurrency = newCurrency
                )
            )
        }
    }

    fun swapCurrencies() {
        _exchangeScreenState.update { state ->
            state.copy(
                currency1 = state.currency2.copy(
                    onCurrencyChange = ::onCurrency1Changed,
                    onAmountChange = ::onAmount1Changed
                ),
                currency2 = state.currency1.copy(
                    onCurrencyChange = ::onCurrency2Changed,
                    onAmountChange = ::onAmount2Changed
                )
            )
        }
    }

    private fun loadQuickAccessPairs() {
        viewModelScope.launch {
            _quickAccessPairs.value = repository.getQuickAccessPairs(userId = 1L)
        }
    }


    fun addToQuickAccessPairs(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            val pair = QuickAccessPairsEntity(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                userId = 1L
            )
            repository.saveQuickAccessPair(pair)
            loadQuickAccessPairs()
        }
    }

    fun onQuickAccessPairClick(pair: QuickAccessPairsEntity) {
        _exchangeScreenState.update { state ->
            state.copy(
                currency1 = state.currency1.copy(selectedCurrency = pair.fromCurrency),
                currency2 = state.currency2.copy(selectedCurrency = pair.toCurrency)
            )
        }
        incrementUsageCount(pair.id)
    }

    private fun incrementUsageCount(pairId: Long) {
        viewModelScope.launch {
            repository.incrementUsageCount(pairId)
        }
    }

    fun deleteQuickAccessPair(pair: QuickAccessPairsEntity) {
        viewModelScope.launch {
            repository.deleteQuickAccessPair(pair)
            loadQuickAccessPairs()
        }
    }

    fun handleDoneClick(apiKey: String) {
        viewModelScope.launch {
            val currentState = _exchangeScreenState.value
            val amountDouble = currentState.currency1.amount.toDoubleOrNull()

            if (currentState.currency1.amount.isBlank() || amountDouble == null || amountDouble <= 0) {
                _errorMessage.value = "Please enter a valid amount greater than 0."
                return@launch
            }

            _exchangeScreenState.update { it.copy(isLoading = true) }

            try {
                val response = repository.convertCurrencies(
                    accessKey = apiKey,
                    from = currentState.currency1.selectedCurrency,
                    to = currentState.currency2.selectedCurrency,
                    amount = amountDouble
                )

                if (response.isSuccessful) {
                    val result = response.body()
                    result?.let { conversionResult ->
                        // Получаем rateForAmount для целевой валюты
                        val targetCurrency = currentState.currency2.selectedCurrency
                        val rateInfo = conversionResult.rates[targetCurrency]

                        if (rateInfo != null) {
                            val convertedAmount = rateInfo.rateForAmount

                            // Сохраняем запись о конвертации
                            val historyEntity = ConversionHistoryEntity(
                                fromCurrency = currentState.currency1.selectedCurrency,
                                toCurrency = currentState.currency2.selectedCurrency,
                                amount = currentState.currency1.amount,
                                convertedValue = convertedAmount
                            )

                            // Сохраняем в базу данных и получаем ID записи
                            val conversionId = repository.insertConversionHistory(historyEntity)

                            // Сохраняем информацию о курсе обмена
                            val rateRecord = ConversionRateRecordEntity(
                                conversionId = conversionId,
                                rateAtConversion = rateInfo.rate,
                                timestamp = System.currentTimeMillis()
                            )
                            repository.insertConversionRateRecord(rateRecord)

                            // Обновляем UI
                            _exchangeScreenState.update { state ->
                                state.copy(
                                    currency2 = state.currency2.copy(
                                        amount = convertedAmount
                                    )
                                )
                            }
                        } else {
                            _errorMessage.value = "Could not find conversion rate for $targetCurrency"
                        }
                    } ?: run {
                        _errorMessage.value = "Conversion response was empty."
                    }
                } else {
                    _errorMessage.value = "Conversion failed with error code ${response.code()}."
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            } finally {
                _exchangeScreenState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }
}