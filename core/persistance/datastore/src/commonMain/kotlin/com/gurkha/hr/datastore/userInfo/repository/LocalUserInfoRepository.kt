package com.gurkha.hr.datastore.userInfo.repository

import com.gurkha.hr.datastore.userInfo.local.UserInfoDataStore
import com.gurkha.hr.datastore.userInfo.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserInfoRepository(
    private val userInfoDataStore: UserInfoDataStore
) : UserInfoRepository {
    override val userInfo: Flow<UserInfo> = userInfoDataStore.userInfoFlow.map {user->
        user.copy(
            isFirstTime = user.isFirstTime ?: true
        )
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        userInfoDataStore.save(
            userInfo = userInfo
        )
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        userInfoDataStore.update(
            userInfo = userInfo
        )
    }
}
