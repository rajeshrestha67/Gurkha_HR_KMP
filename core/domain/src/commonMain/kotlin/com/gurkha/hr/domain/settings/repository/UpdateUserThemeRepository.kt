package com.gurkha.hr.domain.settings.repository

interface UpdateUserThemeRepository {

    suspend fun updateUserTheme(themeMode: Int)
}