package com.gurkha.hr.domain.splash

import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.firstOrNull


class UpdateFirstTimeCheckUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke() {
        val user = userInfoRepository.userInfoFlow.firstOrNull() ?: UserInfo()
        userInfoRepository.saveUserInfo(user.copy(isFirstTime = false))
    }
}

