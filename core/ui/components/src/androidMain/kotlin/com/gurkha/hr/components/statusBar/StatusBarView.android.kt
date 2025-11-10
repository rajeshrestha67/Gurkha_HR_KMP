package com.gurkha.hr.components.statusBar

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.toArgb

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@Composable
actual fun StatusBarView() {
    val activity = LocalActivity.current as? ComponentActivity ?: return
    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

    DisposableEffect(isDarkMode) {
        activity.enableEdgeToEdge(
            statusBarStyle = if (isDarkMode) {
                SystemBarStyle.dark(
                    scrim = backgroundColor,
                )
            } else {
                SystemBarStyle.light(
                    scrim = backgroundColor,
                    darkScrim = backgroundColor
                )
            },
            navigationBarStyle = if (isDarkMode) {
                SystemBarStyle.dark(
                    scrim = backgroundColor,
                )
            } else {
                SystemBarStyle.light(
                    scrim = backgroundColor,
                    darkScrim = backgroundColor
                )
            }

        )

        onDispose { }
    }
}