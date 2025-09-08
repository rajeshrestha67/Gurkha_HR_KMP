package com.gurkha.hr.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
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