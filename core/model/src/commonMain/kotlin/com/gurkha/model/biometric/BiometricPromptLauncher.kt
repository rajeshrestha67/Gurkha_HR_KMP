package com.gurkha.model.biometric


/**
usage
val launcher = rememberBiometricPromptLauncher(
onResult = { result ->
val authStatus = when (result) {
BiometricAuthResult.Success -> "✅ Login Successful!"
is BiometricAuthResult.Error -> "❌ Error: ${result.message}"
BiometricAuthResult.Failure -> "❌ Authentication Failed. Try again."
BiometricAuthResult.NotAvailable -> "⚠️ Biometrics Not Available or Set Up."
}
println("authStatus $authStatus")
}
)

println("launcher.isAvailable ${launcher.isAvailable}")
launcher.launch("title", "desc", "cancel")

 */
data class BiometricPromptLauncher(
    val isAvailable: Boolean,
    val launch: (title: String, subtitle: String, negativeButtonText: String) -> Unit
)