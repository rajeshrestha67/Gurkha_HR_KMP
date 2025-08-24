package com.gurkha.hr.domain.auth.login

import com.gurkha.model.auth.login.UserDto

interface UserRemoteRepository {

    suspend fun login(username: String, password: String): Result<String>
}