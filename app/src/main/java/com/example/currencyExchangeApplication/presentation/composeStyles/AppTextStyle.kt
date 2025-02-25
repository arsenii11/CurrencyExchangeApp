package com.example.currencyExchangeApplication.presentation.composeStyles

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.currencyExchangeApplication.presentation.utilities.composeStyles.AppColor

object AppTextStyle {
    val largeTitle: TextStyle
        @Composable get() = TextStyle(
            fontSize = 34.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = AppColor.text,
            lineHeight = 40.sp,
        )

    val h1: TextStyle
        @Composable get() = TextStyle(
            fontSize = 28.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            color = AppColor.text,
            lineHeight = 33.sp,
        )

    val h2: TextStyle
        @Composable get() = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = AppColor.text,
            lineHeight = 24.sp,
        )
    val h3: TextStyle
        @Composable get() = TextStyle(
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = AppColor.text,
            lineHeight = 32.sp,
        )
    val h4: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = AppColor.text,
            lineHeight = 19.sp,
        )
    val h5: TextStyle
        @Composable get() = TextStyle(
            fontSize = 13.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = AppColor.text,
            lineHeight = 18.sp,
        )
    val body: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            color = AppColor.text,
            lineHeight = 24.sp,
        )
    val bodySmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 13.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            color = AppColor.text,
            lineHeight = 18.sp,
        )
}