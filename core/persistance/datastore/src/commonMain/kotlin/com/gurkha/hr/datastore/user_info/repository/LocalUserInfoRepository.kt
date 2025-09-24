package com.gurkha.hr.datastore.user_info.repository

import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import com.gurkha.hr.datastore.user_info.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserInfoRepository(
    private val userInfoDataStore: UserInfoDataStore
) : UserInfoRepository {
    override val userInfo: Flow<UserInfo> = userInfoDataStore.userInfoFlow.map { user ->
        user.copy(
            isFirstTime = user.isFirstTime ?: true
        )
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        userInfoDataStore.update(
            userInfo = userInfo
        )
    }
}
