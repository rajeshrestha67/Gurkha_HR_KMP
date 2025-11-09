package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.flow.Flow

class FetchTokenAllValueUseCase(
    private val tokenRepository: TokenRepository
) {
     operator fun invoke(): Flow<Token> {
        val currentToken = tokenRepository.token
        return currentToken
    }
}