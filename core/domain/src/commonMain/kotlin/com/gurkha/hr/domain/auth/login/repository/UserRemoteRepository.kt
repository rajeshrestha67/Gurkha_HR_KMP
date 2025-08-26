package com.gurkha.hr.domain.auth.login.repository

import com.gurkha.hr.networkhelper.ERPError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.auth.login.LoginResponseDto

interface UserRemoteRepository {

    suspend fun login(username: String, password: String): ERPResult<LoginResponseDto, ERPError>
}