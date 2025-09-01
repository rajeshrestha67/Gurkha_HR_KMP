package com.gurkha.hr.components

actual class PlatformMessage() {
    actual fun showToast(message: String) {
        println("message")
    }
}