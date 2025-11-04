package com.gurkha.hr.components.biometric

import androidx.compose.runtime.Composable
import com.gurkha.model.biometric.BiometricAuthResult
import com.gurkha.model.biometric.BiometricPromptLauncher

@Composable
expect fun rememberBiometricPromptLauncher(
    onResult: (BiometricAuthResult) -> Unit
): BiometricPromptLauncher