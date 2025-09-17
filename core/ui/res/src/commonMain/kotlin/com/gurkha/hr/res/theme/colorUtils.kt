package com.gurkha.hr.res.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun extendedColor(light: Color, dark: Color): Color {
    return if (isSystemInDarkTheme()) dark else light
}

val ColorScheme.primaryTextColor: Color
    @Composable get() =
        extendedColor(
            light = Color(0xFF727272),
            dark = Color(0xFF212121)
        )

val ColorScheme.onBoardingIndicatorSelectedColor: Color
    @Composable get() = extendedColor(
        light = Color.Red,
        dark = Color.Red
    )

val ColorScheme.onBoardingIndicatorUnSelectedColor: Color
    @Composable get() = extendedColor(
        light = Color.Gray,
        dark = Color.Gray
    )
val ColorScheme.BorderColor: Color
    @Composable get() = extendedColor(
        light = Color.LightGray,
        dark = Color.LightGray
    )

val ColorScheme.linkColor: Color
    @Composable get() = extendedColor(
        light = Color(color =  0xFF0288D1),
        dark = Color(color =  0xFF0288D1)
    )


@Composable
fun ColorScheme.getOnBoardingIndicatorColor(isSelected: Boolean): Color {
    return if (isSelected) onBoardingIndicatorSelectedColor else onBoardingIndicatorUnSelectedColor
}