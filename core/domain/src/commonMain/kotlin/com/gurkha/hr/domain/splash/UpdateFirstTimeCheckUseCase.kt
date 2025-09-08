package com.gurkha.hr.domain.splash

import com.gurkha.hr.datastore.userInfo.model.UserInfo
import com.gurkha.hr.datastore.userInfo.repository.UserInfoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull


class UpdateFirstTimeCheckUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke() {
        val user = userInfoRepository.userInfo.firstOrNull() ?: UserInfo(isFirstTime = true)

        if (user.isFirstTime == true) {
            userInfoRepository.updateUserInfo(user.copy(isFirstTime = false))
        }
    }
}

