package com.gurkha.hr.domain.splash

import com.gurkha.hr.datastore.user_info.model.UserInfo
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import kotlinx.coroutines.flow.firstOrNull


class UpdateFirstTimeCheckUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke() {
        val user = userInfoRepository.userInfo.firstOrNull() ?: UserInfo(isFirstTime = true)

        if (user.isFirstTime == true) {
            userInfoRepository.saveUserInfo(user.copy(isFirstTime = false))
        }
    }
}

