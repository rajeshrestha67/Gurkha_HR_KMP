package com.gurkha.hr.domain.app.repository

import com.gurkha.model.user_info.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserThemeModeRepository {
    val userInfo: Flow<UserInfo>
    suspend fun updateThemeMode(themeMode: Int)

}