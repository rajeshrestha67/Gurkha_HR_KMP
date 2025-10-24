package com.gurkha.hr.components.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.InternalComposeUiApi
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

@OptIn(InternalComposeUiApi::class)
actual object LocalAppLocale {
    private const val LANG_KEY = "AppleLanguages"

    // Default language from system
    private val default: String
        get() = NSLocale.preferredLanguages.firstOrNull() as? String ?: "en"

    private val LocalAppLocale = staticCompositionLocalOf { default }

    actual val current: String
        @Composable get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLang = when (value) {
            null -> default
            "ne-rNP" -> "ne" // normalize for iOS
            else -> value
        }


        // Save selected language in UserDefaults
        val userDefaults = NSUserDefaults.standardUserDefaults
        userDefaults.setObject(newLang, LANG_KEY)

        // Provide new language to Composition
        return LocalAppLocale.provides(newLang)
    }
}