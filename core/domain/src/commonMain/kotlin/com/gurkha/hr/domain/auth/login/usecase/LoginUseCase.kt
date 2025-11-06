package com.gurkha.hr.domain.auth.login.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.auth.login.mapper.toData
import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.firstOrNull

class LoginUseCase(
    private val userRemoteRepository: UserRemoteRepository,
    private val tokenRepository: TokenRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String? = null,
        biometricToken: String? = null
    ): ERPResult<LoginData, DataError> {
        return userRemoteRepository.login(username, password, biometricToken).map {
            it.toData()
        }.onSuccess { data ->
            val token = tokenRepository.token.firstOrNull() ?: Token()
            tokenRepository.saveToken(
                token.copy(
                    jwtToken = data.token
                )
            )

            val userData = userDataRepository.userDataFlow.firstOrNull() ?: UserData()
            userDataRepository.saveUserData(userData.copy(email = username))
        }
    }


}