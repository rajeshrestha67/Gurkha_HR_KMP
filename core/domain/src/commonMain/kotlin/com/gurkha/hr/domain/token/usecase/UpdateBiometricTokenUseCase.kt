package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull

class UpdateBiometricTokenUseCase(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        biometricToken: String
    ){
        val token = tokenRepository.token.firstOrNull() ?: Token()
        tokenRepository.saveToken(token.copy(
            biometricToken = biometricToken
        ))
    }
}