package com.gurkha.hr.domain.settings.usecase

import com.gurkha.hr.components.locale.erpAppLocale
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.firstOrNull

class UpdateUserLanguageUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(langCode: String) {
        erpAppLocale = langCode
        val userInfo = userInfoRepository.userInfo.firstOrNull() ?: UserInfo()
        userInfoRepository.saveUserInfo(userInfo.copy(langCode = langCode))
    }
}