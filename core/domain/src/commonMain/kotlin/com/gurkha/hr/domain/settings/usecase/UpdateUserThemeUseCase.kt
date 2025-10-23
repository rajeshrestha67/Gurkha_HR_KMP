package com.gurkha.hr.domain.settings.usecase

import com.gurkha.hr.domain.settings.repository.UpdateUserThemeRepository

class UpdateUserThemeUseCase(
    private val updateUserThemeRepository: UpdateUserThemeRepository
) {
    suspend operator fun invoke(themeMode: Int) {
        updateUserThemeRepository.updateUserTheme(themeMode = themeMode)
    }
}