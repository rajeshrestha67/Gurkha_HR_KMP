package com.gurkha.hr.components.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.gurkha.model.biometric.BiometricAuthResult
import com.gurkha.model.biometric.BiometricPromptLauncher
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicy
import platform.LocalAuthentication.kLAErrorAuthenticationFailed
import platform.LocalAuthentication.kLAErrorBiometryNotAvailable
import platform.LocalAuthentication.kLAErrorUserCancel
import platform.LocalAuthentication.kLAErrorUserFallback
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberBiometricPromptLauncher(
    onResult: (BiometricAuthResult) -> Unit
): BiometricPromptLauncher {
    val scope = rememberCoroutineScope()

    // Check availability only once and remember the result
    val isAvailable = remember {
//        val context = LAContext()
//        var error: platform.Foundation.NSError? = null
//
//        // Check if the device can authenticate using biometrics (or passcode if allowed by policy)
//        context.canEvaluatePolicy(
//            LAPolicy.DeviceOwnerAuthenticationWithBiometrics,
//            error.ptr
//        )
        val laContext = LAContext()
        laContext.localizedReason = "promptDescription"
        laContext.localizedFallbackTitle = "Cancel"
        // laContext.canEvaluatePolicy(LAPolicy.DeviceOwnerAuthenticationWithBiometrics, null)
        false
    }

    // The actual launcher function
    val launcher: (String, String, String) -> Unit = remember(onResult) {
        { title, subtitle, negativeButtonText ->

            if (!isAvailable) {
                onResult(BiometricAuthResult.NotAvailable)
                return@remember
            }

            // Launch the suspending authentication inside a Coroutine
            scope.launch {
                val result = runBiometricAuth(
                    // iOS uses the subtitle/reason string as the main explanation
                    reason = subtitle
                )
                onResult(result)
            }
        }
    }

    return BiometricPromptLauncher(isAvailable, launcher)
}

private suspend fun runBiometricAuth(
    reason: String
): BiometricAuthResult = suspendCancellableCoroutine { continuation ->

    val context = LAContext()

    context.evaluatePolicy(
        policy = LAPolicy.MAX_VALUE,
        localizedReason = reason,
        reply = { success, laError ->

            // Ensure we resume the coroutine only if it hasn't been cancelled
            if (!continuation.isActive) return@evaluatePolicy

            if (success) {
                continuation.resume(BiometricAuthResult.Success)
            } else if (laError != null) {
                // Handle different error codes returned by LocalAuthentication
                continuation.resume(
                    when (laError.code.toInt()) {
                        kLAErrorUserCancel -> BiometricAuthResult.Error(
                            laError.code.toInt(),
                            "User Cancelled"
                        )

                        kLAErrorAuthenticationFailed -> BiometricAuthResult.Failure
                        kLAErrorBiometryNotAvailable -> BiometricAuthResult.NotAvailable
                        kLAErrorUserFallback -> BiometricAuthResult.Error(
                            laError.code.toInt(),
                            "User Fallback to PIN/Password"
                        )

                        else -> BiometricAuthResult.Error(
                            laError.code.toInt(),
                            laError.localizedDescription
                        )
                    }
                )
            } else {
                // Treat unexpected non-success, non-error result as a failure
                continuation.resume(BiometricAuthResult.Failure)
            }
        }
    )

    // Handle coroutine cancellation (though LAContext doesn't expose a direct cancel method)
    continuation.invokeOnCancellation {
        // Since LAContext doesn't have a public cancel API, we just ensure
        // the callback won't resume the continuation if it's already cancelled.
    }
}