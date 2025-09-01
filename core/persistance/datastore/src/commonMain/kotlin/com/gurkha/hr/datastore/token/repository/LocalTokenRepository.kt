package com.gurkha.hr.datastore.token.repository

import com.gurkha.hr.datastore.token.local.TokenDataStore
import com.gurkha.hr.datastore.token.model.Token
import kotlinx.coroutines.flow.Flow

class LocalTokenRepository(
    private val tokenDataStore: TokenDataStore
) : TokenRepository {
    override val token: Flow<Token> = tokenDataStore.tokenFlow
    override suspend fun saveToken(token: Token) {
        tokenDataStore.update(token = token)
    }

}