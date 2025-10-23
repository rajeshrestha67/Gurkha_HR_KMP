package com.gurkha.hr.data.settings

import com.gurkha.hr.domain.app.repository.UserThemeModeRepository
import com.gurkha.hr.domain.settings.repository.UpdateUserThemeRepository

class UpdateUserThemeRepositoryImpl(
    private val userThemeModeRepository: UserThemeModeRepository
) : UpdateUserThemeRepository {
    override suspend fun updateUserTheme(themeMode: Int) {
        userThemeModeRepository.updateThemeMode(themeMode = themeMode)
    }
}