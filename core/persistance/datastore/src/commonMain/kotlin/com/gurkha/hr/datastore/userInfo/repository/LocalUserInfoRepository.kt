package com.gurkha.hr.datastore.userInfo.repository

import com.gurkha.hr.datastore.userInfo.local.UserInfoDataStore
import com.gurkha.hr.datastore.userInfo.model.UserInfo
import kotlinx.coroutines.flow.Flow

class LocalUserInfoRepository(
    private val userInfoDataStore: UserInfoDataStore
) : UserInfoRepository {
    override val userInfo: Flow<UserInfo> = userInfoDataStore.userInfoFlow

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        userInfoDataStore.update(
            userInfo = userInfo
        )
    }
}
