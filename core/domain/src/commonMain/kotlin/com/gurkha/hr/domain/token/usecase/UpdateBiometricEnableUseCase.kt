package com.gurkha.hr.domain.token.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull

class UpdateBiometricEnableUseCase(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(isEnable: Boolean){
        val currentToken = tokenRepository.token.firstOrNull() ?: Token()
        tokenRepository.saveToken(token = currentToken.copy(
            isBiometricEnable = isEnable
        ))
    }
}