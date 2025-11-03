package com.gurkha.hr.domain.auth.login.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.auth.login.LoginResponseDto
import com.gurkha.model.network.DataError

class FakeUserRemoteRepository : UserRemoteRepository {

    private var shouldReturnError = false
    private var shouldReturnNetworkError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun login(
        username: String,
        password: String
    ): ERPResult<LoginResponseDto, DataError> {
        return when {
            shouldReturnNetworkError -> ERPResult.Error(DataError.NetworkError.DataUnknown)
            shouldReturnError -> ERPResult.Error(DataError.NetworkError.Custom("Simulated error"))
            else -> ERPResult.Success(LoginResponseDto(token = "fakeToken", role = "HRM_EMPLOYEE"))
        }
    }
}