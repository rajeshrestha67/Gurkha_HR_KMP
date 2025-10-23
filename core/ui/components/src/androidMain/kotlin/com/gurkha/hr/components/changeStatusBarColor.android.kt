package com.gurkha.hr.components

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat

@Composable
actual fun ChangeStatusBarColor(
    darkIcons: Boolean
) {
    val activity = LocalActivity.current
    println("activity $activity")
    DisposableEffect(darkIcons) {
        changeColor(activity = activity, darkIcons = darkIcons)
        onDispose {

        }
    }
}

private fun changeColor(activity: Activity?, darkIcons: Boolean) {
    val window = activity?.window ?: return
    val view = window.decorView

    WindowCompat.getInsetsController(window, view).apply {
        isAppearanceLightStatusBars = darkIcons
    }
}