// File: com/example/currencyExchangeApplication/presentation/history/HistoryViewModel.kt

package com.example.currencyExchangeApplication.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.database.entities.ConversionHistoryEntity
import com.example.currencyExchangeApplication.data.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository
) : ViewModel() {

    /**
     * LiveData emitting a list of [ConversionHistoryEntity].
     */
    val readAllData: LiveData<List<ConversionHistoryEntity>> = exchangeRepository.getLatestTransactions()

    /**
     * LiveData emitting a list of [HistoryListItem].
     */
    val historyWithRates: LiveData<List<HistoryListItem>> = exchangeRepository.getHistoryWithRates()

    // LiveData для отслеживания успешного удаления всех записей
    private val _deleteAllSuccess = MutableLiveData<Boolean>(false)
    val deleteAllSuccess: LiveData<Boolean> = _deleteAllSuccess

    /**
     * Deletes all conversion history records.
     */
    fun deleteAll() {
        viewModelScope.launch {
            exchangeRepository.deleteAllConversions()
            _deleteAllSuccess.value = true
        }
    }

    /**
     * Сбрасывает флаг успешного удаления.
     */
    fun resetDeleteAllSuccess() {
        _deleteAllSuccess.value = false
    }
}
