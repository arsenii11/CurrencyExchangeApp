// File: com/example/currencyExchangeApplication/presentation/homescreen/HomeViewModel.kt

package com.example.currencyExchangeApplication.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyExchangeApplication.data.database.entities.UserEntity
import com.example.currencyExchangeApplication.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    // StateFlow для хранения текущего пользователя
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    init {
        // Загрузка существующего пользователя при инициализации ViewModel
        viewModelScope.launch {
            _user.value = repository.fetchUser()
        }
    }

    /**
     * Сохраняет пользователя в базе данных и обновляет состояние.
     */
    fun saveUser(nickname: String, email: String = "") {
        viewModelScope.launch {
            repository.saveUser(nickname, email)
            _user.value = repository.fetchUser() // Обновление данных пользователя
        }
    }

    /**
     * Удаляет пользователя из базы данных и очищает состояние.
     */
    fun deleteUser() {
        viewModelScope.launch {
            repository.deleteUser()
            _user.value = null // Очистка данных пользователя
        }
    }
}
