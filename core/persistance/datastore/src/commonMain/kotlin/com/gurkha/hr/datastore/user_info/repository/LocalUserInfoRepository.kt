package com.gurkha.hr.datastore.user_info.repository

import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow

class LocalUserInfoRepository(
    private val userInfoDataStore: UserInfoDataStore
) : UserInfoRepository {
    override val userInfoFlow: Flow<UserInfo> = userInfoDataStore.userInfoFlow

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        userInfoDataStore.update(
            userInfo = userInfo
        )
    }
}
