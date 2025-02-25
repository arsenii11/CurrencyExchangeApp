package com.example.currencyExchangeApplication.presentation.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun HomeScreen(
    onStartClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    // Сбор состояния пользователя из ViewModel
    val user by viewModel.user.collectAsStateWithLifecycle()

    // Локальное состояние для ввода имени пользователя
    var enteredNickname by remember { mutableStateOf("") }

    // Отображение Snackbar при необходимости

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        },
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Если пользователь уже существует, показать приветствие
                if (user != null) {
                    Text(
                        text = "Welcome, ${user?.userName}!",
                        style = MaterialTheme.typography.h6
                    )
                } else {
                    // Если нет пользователя, показать форму ввода
                    Text(
                        text = "Please enter your nickname:",
                        style = MaterialTheme.typography.subtitle1
                    )
                    OutlinedTextField(
                        value = enteredNickname,
                        onValueChange = { newValue -> enteredNickname = newValue },
                        label = { Text("Nickname") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // "Start" кнопка доступна только если введено имя
                Button(
                    onClick = {
                        // Сохранить имя пользователя через ViewModel
                        viewModel.saveUser(enteredNickname)
                        // Навигация на следующий экран
                        onStartClick(enteredNickname)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    enabled = enteredNickname.isNotEmpty() || user != null
                ) {
                    Text("Start", style = MaterialTheme.typography.button)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    MaterialTheme {
        HomeScreen(
            onStartClick = { nickname ->
                // Действие при нажатии на "Start"
                println("Starting with nickname: $nickname")
            },
            viewModel = hiltViewModel()
        )
    }
}
