package com.gurkha.hr.domain.auth.login.usecase

import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository

class ClearTokenUseCase(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke() {
        tokenRepository.saveToken(Token())
    }

}