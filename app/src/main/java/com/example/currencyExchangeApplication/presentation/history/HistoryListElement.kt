package com.example.currencyExchangeApplication.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.room.ColumnInfo

data class HistoryListItem(
    val fromCurrency: String,
    val toCurrency: String,
    val amount: String,
    val convertedValue: String,
    @ColumnInfo(name = "rateAtConversion") val conversionRate: String = "N/A"
)


@Composable
fun HistoryListElement(historyListItem: HistoryListItem) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clickable { showDialog = true },  // Открытие диалога по клику
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "From:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(Modifier.weight(1f)) {
                    Text(text = historyListItem.fromCurrency, fontSize = 14.sp)
                    Text(text = historyListItem.amount, fontSize = 14.sp, color = Color.Gray)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "To:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp)) {
                    Text(text = historyListItem.toCurrency, fontSize = 14.sp)
                    Text(
                        text = historyListItem.convertedValue,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    // Диалоговое окно для отображения курса обмена
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Conversion Rate Details") },
            text = {
                Text(
                    text = "Conversion Rate: ${historyListItem.conversionRate}",
                    fontSize = 16.sp
                )
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryListElement() {
    val sampleItem = HistoryListItem(
        fromCurrency = "USD",
        toCurrency = "EUR",
        amount = "100",
        convertedValue = "85",
        conversionRate = "erfe"
    )
    HistoryListElement(historyListItem = sampleItem)
}
