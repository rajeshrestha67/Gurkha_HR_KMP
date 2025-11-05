package com.gurkha.hr.domain.auth.login.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull

class ClearTokenUseCase(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke() {
        val token = tokenRepository.token.firstOrNull() ?: Token()
        tokenRepository.saveToken(token.copy(
            jwtToken = null
        ))
    }

}