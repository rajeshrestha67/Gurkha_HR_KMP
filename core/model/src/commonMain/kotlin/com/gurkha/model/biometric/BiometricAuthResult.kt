package com.gurkha.model.biometric

sealed interface BiometricAuthResult {
    data object Success : BiometricAuthResult
    data class Error(val code: Int, val message: String) : BiometricAuthResult
    data object Failure : BiometricAuthResult
    data object NotAvailable : BiometricAuthResult
}