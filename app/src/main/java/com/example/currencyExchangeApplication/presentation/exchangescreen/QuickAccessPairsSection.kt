package com.example.currencyExchangeApplication.presentation.exchangescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyExchangeApplication.data.database.entities.QuickAccessPairsEntity
import com.example.currencyExchangeApplication.presentation.composeStyles.AppTextStyle

@Composable
fun QuickAccessPairsSection(
    quickAccessPairs: List<QuickAccessPairsEntity>,
    onQuickAccessPairClick: (QuickAccessPairsEntity) -> Unit,
    onDeletePairClick: (QuickAccessPairsEntity) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Quick Access Pairs",
            style = AppTextStyle.h2,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Replace LazyColumn with Column
        Column {
            quickAccessPairs.forEach { pair ->
                QuickAccessPairItem(
                    pair = pair,
                    onClick = { onQuickAccessPairClick(pair) },
                    onDeleteClick = { onDeletePairClick(pair) }
                )
                Divider()
            }
        }
    }
}


@Composable
fun QuickAccessPairItem(
    pair: QuickAccessPairsEntity,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${pair.fromCurrency} -> ${pair.toCurrency}",
            style = MaterialTheme.typography.body1
        )

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete pair"
            )
        }
    }
}