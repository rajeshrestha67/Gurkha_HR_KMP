package com.gurkha.hr.datastore.user_info.repository

import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    val userInfoFlow: Flow<UserInfo>

    suspend fun saveUserInfo(userInfo: UserInfo)
}