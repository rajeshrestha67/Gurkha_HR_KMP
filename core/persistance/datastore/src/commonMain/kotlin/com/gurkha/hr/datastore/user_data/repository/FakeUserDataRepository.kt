package com.gurkha.hr.datastore.user_data.repository

import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeUserDataRepository() : UserDataRepository {
    private val _userDataFlow = MutableStateFlow(UserData())
    override val userDataFlow: Flow<UserData> = _userDataFlow.asStateFlow()
    override suspend fun saveUserData(userData: UserData) {
        _userDataFlow.emit(userData)
    }
}