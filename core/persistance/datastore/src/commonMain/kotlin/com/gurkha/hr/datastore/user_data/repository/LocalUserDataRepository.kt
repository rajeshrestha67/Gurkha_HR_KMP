package com.gurkha.hr.datastore.user_data.repository

import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.Flow

class LocalUserDataRepository(
    private val userDataDataStore: UserDataDataStore
) : UserDataRepository {
    override val userDataFlow: Flow<UserData> = userDataDataStore.userInfoFlow
    override suspend fun saveUserData(userData: UserData) {
        userDataDataStore.update(userData = userData)
    }
}