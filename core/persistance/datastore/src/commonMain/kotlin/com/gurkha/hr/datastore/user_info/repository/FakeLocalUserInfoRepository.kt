package com.gurkha.hr.datastore.user_info.repository

import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeLocalUserInfoRepository : UserInfoRepository {

    private val _userInfoFlow = MutableStateFlow(UserInfo())

    override val userInfoFlow: Flow<UserInfo> = _userInfoFlow.asStateFlow()

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        _userInfoFlow.value = userInfo
    }
}