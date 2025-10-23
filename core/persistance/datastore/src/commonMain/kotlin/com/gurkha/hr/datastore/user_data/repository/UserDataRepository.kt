package com.gurkha.hr.datastore.user_data.repository

import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userDataFlow: Flow<UserData>

    suspend fun saveUserData(userData: UserData)

}