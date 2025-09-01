package com.gurkha.hr.datastore.token.local

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.gurkha.hr.datastore.token.model.Token
import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class TokenDataStore(
    private val produceFilePath: () -> String
) {

    private val db = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = TokenJsonSerializer,
            producePath = {
                produceFilePath().toPath()
            },
        ),
    )

    val tokenFlow: Flow<Token>
        get() = db.data

    suspend fun update(token: Token) {
        db.updateData { _ ->
            token
        }
    }

}