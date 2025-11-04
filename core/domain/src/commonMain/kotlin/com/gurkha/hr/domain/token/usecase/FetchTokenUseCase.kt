package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull

class FetchTokenUseCase(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): String? {
        return tokenRepository.token.firstOrNull()?.jwtToken
    }
}