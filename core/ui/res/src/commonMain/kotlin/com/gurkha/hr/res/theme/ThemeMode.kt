package com.gurkha.hr.res.theme

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

enum class ThemeMode(val value: Int, val title: StringResource) {
    LIGHT(value = 0, title = SharedRes.Strings.light),
    DARK(value = 1, title = SharedRes.Strings.dark),
    SYSTEM(value = 2, title = SharedRes.Strings.system);

    companion object {
        private val typeMap =
            enumValues<ThemeMode>().associateBy { it.value }

        fun get(value: Int?): ThemeMode = ThemeMode.typeMap[value] ?: SYSTEM

        val list: List<ThemeMode>
            get() = entries.toList().map { it }
    }
}