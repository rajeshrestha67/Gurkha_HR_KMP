package com.gurkha.hr.components.biometric

import androidx.activity.compose.LocalActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.gurkha.model.biometric.BiometricAuthResult
import com.gurkha.model.biometric.BiometricPromptLauncher
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
actual fun rememberBiometricPromptLauncher(
    onResult: (BiometricAuthResult) -> Unit
): BiometricPromptLauncher {


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val activity = LocalActivity.current as FragmentActivity

    val isAvailable = remember {
        val manager = BiometricManager.from(context)
        manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }
    val launcher: (String, String, String) -> Unit = remember {
        { title, subtitle, negativeButtonText ->
            if (!isAvailable) {
                onResult(BiometricAuthResult.NotAvailable)
                return@remember
            }

            // Launch the suspending authentication inside a Coroutine
            scope.launch {
                val result = runBiometricAuth(
                    activity, title, subtitle, negativeButtonText
                )
                onResult(result)
            }
        }
    }

    return BiometricPromptLauncher(isAvailable, launcher)
}

private suspend fun runBiometricAuth(
    activity: FragmentActivity,
    title: String,
    subtitle: String,
    negativeButtonText: String
): BiometricAuthResult = suspendCancellableCoroutine { continuation ->

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setAllowedAuthenticators(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        .build()

    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            if (continuation.isActive) continuation.resume(BiometricAuthResult.Success)
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            if (continuation.isActive) {
                // If user cancels, Android returns error code 10 (ERROR_USER_CANCELED)
                continuation.resume(BiometricAuthResult.Error(errorCode, errString.toString()))
            }
        }

        override fun onAuthenticationFailed() {
            // We don't resume here as the prompt often retries automatically.
            // If the user exhausts retries, an Error will be returned.
        }
    }

    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor, callback)

    biometricPrompt.authenticate(promptInfo)

    continuation.invokeOnCancellation {
        biometricPrompt.cancelAuthentication()
    }
}