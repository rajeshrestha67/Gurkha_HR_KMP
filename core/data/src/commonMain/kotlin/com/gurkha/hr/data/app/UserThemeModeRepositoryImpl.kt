package com.gurkha.hr.data.app

import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.hr.domain.app.repository.UserThemeModeRepository
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserThemeModeRepositoryImpl(
    private val userInfoRepository: UserInfoRepository,
) : UserThemeModeRepository {
    override val userInfo: Flow<UserInfo> = userInfoRepository.userInfo
    override suspend fun updateThemeMode(themeMode: Int) {
        val userInfo = userInfoRepository.userInfo.firstOrNull() ?: UserInfo()
        userInfoRepository.saveUserInfo(userInfo.copy(userThemeMode = themeMode))
    }
}