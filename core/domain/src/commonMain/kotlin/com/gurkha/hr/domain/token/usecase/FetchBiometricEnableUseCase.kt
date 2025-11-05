package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.flow.Flow

class FetchBiometricEnableUseCase(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Flow<Token> {
        val currentToken = tokenRepository.token
        return currentToken
    }
}