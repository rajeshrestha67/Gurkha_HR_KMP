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
        light = Color(color = 0xFF0288D1),
        dark = Color(color = 0xFF0288D1)
    )
val ColorScheme.highLightColor: Color
    @Composable get() = extendedColor(
        light = Color.White.copy(alpha = 0.7f),
        dark = Color.White.copy(alpha = 0.7f)
    )

val ColorScheme.darkPrimaryTextColor: Color
    @Composable get() = extendedColor(
        light = Color.Black,
        dark = Color.Black
    )

val ColorScheme.leaveBalanceBorder: Color
    @Composable get() = extendedColor(
        light = Color.Blue,
        dark = Color.Blue
    )

val ColorScheme.leaveApprovedBorder: Color
    @Composable get() = extendedColor(
        light = Color.Yellow,
        dark = Color.Yellow
    )

val ColorScheme.leavePendingBorder: Color
    @Composable get() = extendedColor(
        light = Color.Green,
        dark = Color.Green
    )

val ColorScheme.leaveCancelledBorder: Color
    @Composable get() = extendedColor(
        light = Color.Red,
        dark = Color.Red
    )

val ColorScheme.veryLightGray: Color
    @Composable get() = extendedColor(
        light = Color(0xFFF3F6F4),
        dark =Color(0xFFF3F6F4),
    )

@Composable
fun ColorScheme.getOnBoardingIndicatorColor(isSelected: Boolean): Color {
    return if (isSelected) onBoardingIndicatorSelectedColor else onBoardingIndicatorUnSelectedColor
}