package com.example.currencyExchangeApplication.presentation.exchangescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.conferoapplication.R
import com.example.currencyExchangeApplication.data.model.ExchangeScreenState
import com.utilities.CurrenciesAvailable
import com.utilities.Links
import com.utilities.Utility


@Composable
fun ExchangeScreen(
    navController: NavController,
    viewModel: ExchangeViewModel = hiltViewModel()
) {
    val exchangeScreenState by viewModel.exchangeScreenState.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    var isInternetAvailable by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isInternetAvailable = Utility.isInternetAvailableWithHttpCheck(context)
        if (!isInternetAvailable) {
            scaffoldState.snackbarHostState.showSnackbar("No connection")
        }
    }


    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            scaffoldState.snackbarHostState.showSnackbar(errorMessage)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Currency Exchange") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Currency Selection Section
                item {
                    CurrencySection(
                        state = exchangeScreenState,
                        onSwapClick = { viewModel.swapCurrencies() },
                        onAddToQuickAccessPairs = {
                            viewModel.addToQuickAccessPairs(
                                fromCurrency = exchangeScreenState.currency1.selectedCurrency,
                                toCurrency = exchangeScreenState.currency2.selectedCurrency
                            )
                        }
                    )
                }

                // Action Buttons
                item {
                    ActionButtons(
                        onDoneClick = { viewModel.handleDoneClick(Links.API_KEY) },
                        onHistoryClick = { navController.navigate("history") },
                        isNetworkAvailable = isInternetAvailable
                    )
                }

                // Loading Indicator
                if (exchangeScreenState.isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }

                // Quick Access Pairs Section
                item {
                    QuickAccessPairsSection(
                        quickAccessPairs = viewModel.quickAccessPairs.collectAsState().value,
                        onQuickAccessPairClick = viewModel::onQuickAccessPairClick,
                        onDeletePairClick = viewModel::deleteQuickAccessPair
                    )
                }
            }
        }
    )
}


@Composable
fun CurrencySection(
    state: ExchangeScreenState,
    onSwapClick: () -> Unit,
    onAddToQuickAccessPairs: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing between rows
    ) {
        // First Currency Row ("From")
        CurrencyRow(
            label = "From",
            selectedCurrency = state.currency1.selectedCurrency,
            currencies = CurrenciesAvailable.currenciesList(),
            amount = state.currency1.amount,
            onCurrencyChange = { newCurrency ->
                state.currency1.onCurrencyChange(newCurrency)
            },
            onAmountChange = { newAmount ->
                state.currency1.onAmountChange(newAmount)
            },
            disabledCurrency = state.currency2.selectedCurrency // Passing the disabled currency
        )

        // Buttons Row: Swap and Quick Pairs
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quick Pairs Button
            Button(
                onClick = onAddToQuickAccessPairs,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .height(40.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Quick Pair",
                    tint = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Quick Pairs",
                    color = MaterialTheme.colors.onSecondary
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Space between buttons

            // Swap Button
            IconButton(
                onClick = onSwapClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_swap), // Ensure you have this drawable
                    contentDescription = "Swap Currencies",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        // Second Currency Row ("To")
        CurrencyRow(
            label = "To",
            selectedCurrency = state.currency2.selectedCurrency,
            currencies = CurrenciesAvailable.currenciesList(),
            amount = state.currency2.amount,
            onCurrencyChange = state.currency2.onCurrencyChange,
            onAmountChange = state.currency2.onAmountChange,
            disabledCurrency = state.currency1.selectedCurrency // Passing the disabled currency
        )
    }
}

@Composable
fun CurrencyRow(
    label: String,
    selectedCurrency: String,
    currencies: List<String>,
    amount: String,
    onCurrencyChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    disabledCurrency: String? = null
) {
    Surface(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Currency Selector с фиксированной шириной
                DropdownMenuComponent(
                    selectedItem = selectedCurrency,
                    items = currencies,
                    onItemSelected = onCurrencyChange,
                    modifier = Modifier.width(120.dp), // Фиксированная ширина
                    disabledItem = disabledCurrency
                )
                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    label = { Text("Amount") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    isError = amount.toDoubleOrNull()?.let { it <= 0 } ?: false
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownMenuComponent(
    selectedItem: String,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    disabledItem: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { /* Не требуется изменять текст вручную */ },
            readOnly = true,
            label = { Text("Currency") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand dropdown"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.filter { it != disabledItem }.forEach { currency ->
                DropdownMenuItem(onClick = {
                    onItemSelected(currency)
                    expanded = false
                }) {
                    Text(text = currency)
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    onDoneClick: () -> Unit,
    onHistoryClick: () -> Unit,
    isNetworkAvailable: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isNetworkAvailable) MaterialTheme.colors.primary else Color.Gray
            ),
            enabled = isNetworkAvailable
        ) {
            Text("Convert", style = MaterialTheme.typography.button)
        }

        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ) {
            Text(
                "View History",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExchangeScreen() {
    MaterialTheme {
        ExchangeScreen(
            navController = rememberNavController(),
            viewModel = hiltViewModel()
        )
    }
}

// Дата класс для параметров превью
data class CurrencyRowParams(
    val label: String,
    val selectedCurrency: String,
    val currencies: List<String>,
    val amount: String,
    val disabledCurrency: String? = null
)

// Провайдер параметров для превью
class CurrencyRowParamsProvider : PreviewParameterProvider<CurrencyRowParams> {
    override val values = sequenceOf(
        CurrencyRowParams(
            label = "Convert from",
            selectedCurrency = "USD",
            currencies = listOf("USD", "EUR", "GBP", "JPY", "RUB"),
            amount = "100.00"
        ),
        CurrencyRowParams(
            label = "Convert to",
            selectedCurrency = "EUR",
            currencies = listOf("USD", "EUR", "GBP", "JPY", "RUB"),
            amount = "92.45",
            disabledCurrency = "USD"
        )
    )
}

@Preview(
    name = "Currency Row Mobile",
    device = "spec:width=360dp,height=800dp,dpi=440"
)
@Preview(
    name = "Currency Row Tablet",
    device = "spec:width=800dp,height=1280dp,dpi=440"
)
@Composable
fun CurrencyRowPreview(
    @PreviewParameter(CurrencyRowParamsProvider::class) params: CurrencyRowParams
) {
    CurrencyRow(
        label = params.label,
        selectedCurrency = params.selectedCurrency,
        currencies = params.currencies,
        amount = params.amount,
        onCurrencyChange = { },
        onAmountChange = { },
        disabledCurrency = params.disabledCurrency
    )
}
