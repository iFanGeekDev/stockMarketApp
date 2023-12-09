package com.example.stockmarketapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color.Green,
    background = DarkBlue,
    onPrimary = Color.DarkGray,
    onBackground = TextWhite
)


@Composable
fun StockMarketAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {


    MaterialTheme(
        colorScheme = DarkColorPalette,
        typography = Typography,
        content = content
    )
}