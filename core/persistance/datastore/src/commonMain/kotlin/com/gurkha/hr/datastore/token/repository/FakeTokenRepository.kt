package com.gurkha.hr.datastore.token.repository

import com.gurkha.hr.datastore.token.model.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTokenRepository() : TokenRepository {
    private val _token = MutableStateFlow(Token())
    override val token: Flow<Token> = _token.asStateFlow()

    override suspend fun saveToken(token: Token) {
        _token.emit(token)
    }
}