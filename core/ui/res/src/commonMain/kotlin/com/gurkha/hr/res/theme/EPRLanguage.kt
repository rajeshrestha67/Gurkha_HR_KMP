package com.gurkha.hr.res.theme

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

enum class EPRLanguage(val langCode: String, val displayName: StringResource) {
    ENGLISH("en", SharedRes.Strings.english),
    Nepali("ne-rNP", SharedRes.Strings.nepali);

    companion object {
        private val typeMap =
            enumValues<EPRLanguage>().associateBy { it.langCode }

        fun get(langCode: String): EPRLanguage = EPRLanguage.typeMap[langCode] ?: ENGLISH

        val list: List<EPRLanguage>
            get() = EPRLanguage.entries.toList().map { it }
    }
}