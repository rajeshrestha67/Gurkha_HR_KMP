package com.gurkha.hr.components.platform_utils

expect class PlatformUtils() {
    fun copyToClipboard(text: String)
    fun shareText(text: String, title: String?)

    fun callPhoneNumber(phoneNumber: String)
}
