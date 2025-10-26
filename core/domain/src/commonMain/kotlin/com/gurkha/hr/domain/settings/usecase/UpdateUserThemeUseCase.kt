package com.gurkha.hr.domain.settings.usecase

import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.firstOrNull

class UpdateUserThemeUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(themeMode: Int) {
        val userInfo = userInfoRepository.userInfo.firstOrNull() ?: UserInfo()
        userInfoRepository.saveUserInfo(userInfo.copy(userThemeMode = themeMode))
    }
}