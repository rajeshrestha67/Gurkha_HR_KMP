package com.gurkha.hr.domain.auth.login.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.domain.auth.login.mapper.toData
import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.firstOrNull

class LoginUseCase(
    private val userRemoteRepository: UserRemoteRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): ERPResult<LoginData, DataError> {
        return userRemoteRepository.login(username, password).map {
            it.toData()
        }.onSuccess {
            val token = tokenRepository.token.firstOrNull() ?: Token()
            println("token $token")
            tokenRepository.saveToken(token.copy("ttest"))
        }
    }
}