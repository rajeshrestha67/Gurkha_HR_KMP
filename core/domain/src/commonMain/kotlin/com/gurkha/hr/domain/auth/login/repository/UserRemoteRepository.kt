package com.gurkha.hr.domain.auth.login.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.auth.login.LoginResponseDto
import com.gurkha.model.network.DataError

interface UserRemoteRepository {

    suspend fun login(username: String, password: String?, biometricToken: String?): ERPResult<LoginResponseDto, DataError>
}