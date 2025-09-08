package com.gurkha.hr.datastore.userInfo.repository

import com.gurkha.hr.datastore.userInfo.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    val userInfo : Flow<UserInfo>

    suspend fun saveUserInfo(userInfo: UserInfo)

//    update the userinfo for the first time login or not
    suspend fun updateUserInfo(userInfo: UserInfo)
}