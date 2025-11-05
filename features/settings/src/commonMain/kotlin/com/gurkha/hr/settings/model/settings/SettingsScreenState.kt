package com.gurkha.hr.settings.model.settings

import com.gurkha.hr.res.theme.EPRLanguage
import com.gurkha.hr.res.theme.ThemeMode

data class SettingsScreenState(
    val items: List<SettingList> = SettingList.list,
    val biometricEnabled: Boolean = false,
    val themes: List<ThemeMode> = ThemeMode.list,
    val languages: List<EPRLanguage> = EPRLanguage.list

)
