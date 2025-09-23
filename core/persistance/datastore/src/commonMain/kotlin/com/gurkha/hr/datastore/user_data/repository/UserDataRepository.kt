package com.gurkha.hr.datastore.user_data.repository

import com.gurkha.hr.datastore.user_data.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userDataFlow: Flow<UserData>

    suspend fun saveUserData(userData: UserData)

}