package com.gurkha.hr.domain.auth.login.usecase

import com.gurkha.hr.domain.auth.login.mapper.toData
import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.networkhelper.ERPError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

class LoginUseCase(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend operator fun invoke(username: String, password: String): ERPResult<LoginData, ERPError> {
        return userRemoteRepository.login(username, password).map {
            it.toData()
        }
    }
}