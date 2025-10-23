package com.gurkha.hr.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController

fun Modifier.hideKeyboardOnTap(
    focusManager: FocusManager? = null,
    keyboardController: SoftwareKeyboardController?
): Modifier {
    return pointerInput(Unit) {
        detectTapGestures {
            focusManager?.clearFocus(true)
            keyboardController?.hide()
        }
    }
}


@Composable
fun isKeyboardVisible(): androidx.compose.runtime.State<Boolean> {
    val ime = WindowInsets.ime
    val isVisible = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    LaunchedEffect(ime) {
        snapshotFlow { ime.getBottom(density) > 0 }
            .collect { isVisible.value = it }
    }
    return isVisible
}