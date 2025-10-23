package com.gurkha.hr.domain.app.usecase

import com.gurkha.hr.domain.app.repository.UserThemeModeRepository
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow

class FetchUserThemeModeUseCase(
    private val userThemeModeRepository: UserThemeModeRepository,
) {
    operator fun invoke(): Flow<UserInfo> {
        return userThemeModeRepository.userInfo
    }
}