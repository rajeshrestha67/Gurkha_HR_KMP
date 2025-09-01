package com.gurkha.hr.datastore.token.repository

import com.gurkha.hr.datastore.token.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    val token: Flow<Token>

    suspend fun saveToken(token: Token)

}