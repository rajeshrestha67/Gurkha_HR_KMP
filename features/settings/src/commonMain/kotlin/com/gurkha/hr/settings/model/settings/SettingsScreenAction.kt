package com.gurkha.hr.settings.model.settings

import com.gurkha.hr.res.theme.EPRLanguage
import com.gurkha.hr.res.theme.ThemeMode

sealed interface SettingsScreenAction {
    data class OnNotificationStatusChange(val enable: Boolean) : SettingsScreenAction
    data class OnThemeSelected(val theme: ThemeMode) : SettingsScreenAction
    data class OnLanguageSelected(val language: EPRLanguage) : SettingsScreenAction
}