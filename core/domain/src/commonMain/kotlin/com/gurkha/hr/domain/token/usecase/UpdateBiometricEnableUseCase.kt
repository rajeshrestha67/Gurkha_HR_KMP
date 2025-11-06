package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository

class UpdateBiometricEnableUseCase(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(token: Token) {
        tokenRepository.saveToken(token)

    }
}