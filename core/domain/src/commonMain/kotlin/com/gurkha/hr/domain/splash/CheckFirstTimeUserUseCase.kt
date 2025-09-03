package com.gurkha.hr.domain.splash

import com.gurkha.hr.datastore.userInfo.repository.UserInfoRepository
import kotlinx.coroutines.flow.firstOrNull

class CheckFirstTimeUserUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(): Boolean {
        return userInfoRepository.userInfo.firstOrNull()?.isFirstTime ?: true
    }
}