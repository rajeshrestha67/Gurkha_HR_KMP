package com.gurkha.hr.domain.auth.login


import com.gurkha.hr.networkhelper.ERPError
import com.gurkha.hr.networkhelper.ERPResult

interface UserRemoteRepository {

    suspend fun login(username: String, password: String): ERPResult<String, ERPError>
}