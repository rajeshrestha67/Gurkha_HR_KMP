package com.gurkha.hr.domain.auth.login.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.auth.login.LoginResponseDto
import com.gurkha.model.network.DataError

class FakeUserRemoteRepository : UserRemoteRepository {

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun login(
        username: String,
        password: String
    ): ERPResult<LoginResponseDto, DataError> {
        return if (shouldReturnError) {
            ERPResult.Error(DataError.NetworkError.Custom("Simulated error"))
        } else {
            ERPResult.Success(LoginResponseDto(token = "fakeToken", role = "HRM_EMPLOYEE"))
        }
    }
}