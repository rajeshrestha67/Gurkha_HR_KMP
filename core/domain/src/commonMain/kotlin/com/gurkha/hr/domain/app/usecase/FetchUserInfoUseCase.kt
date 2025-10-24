package com.gurkha.hr.domain.app.usecase

import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow

class FetchUserInfoUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    operator fun invoke(): Flow<UserInfo> {
        return userInfoRepository.userInfo
    }
}