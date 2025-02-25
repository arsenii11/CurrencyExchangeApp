package com.example.currencyExchangeApplication.presentation.utilities.composeStyles

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.conferoapplication.R


object AppColor {
    object Palette {

        val Yellow100: Color @Composable get() = Color(0xFFFDF5CE)
        val Yellow200: Color @Composable get() = Color(0xFFF5DD80)
        val Black: Color @Composable get() = colorResource(id = R.color.black)
        val White: Color @Composable get() = colorResource(id = R.color.white)
        val Blue: Color @Composable get() = Color(0xFF136FE7)
        val N_01: Color @Composable get() = Color( 0xFF136FE7)
        val N_02: Color @Composable get() = Color( 0xFF136FE7)
        val Transparent: Color @Composable get() = Color.Transparent
    }

    val text: Color @Composable get() = if (!MaterialTheme.colors.isLight) Palette.White else Palette.Black
    val textLight: Color @Composable get() = text.copy(alpha = 0.6F)
    val textUltraLight: Color @Composable get() = text.copy(alpha = 0.3F)
    val toolbar: Color @Composable get() = if (!MaterialTheme.colors.isLight) Palette.N_01 else Palette.White
    val background: Color @Composable get() = if (!MaterialTheme.colors.isLight) Palette.N_01 else Palette.White
    val backgroundFloat: Color @Composable get() = if (!MaterialTheme.colors.isLight) Palette.N_02 else Palette.White
    val backgroundSecondary: Color @Composable get() = if (!MaterialTheme.colors.isLight) Palette.Black else Palette.N_02

}
