package com.gurkha.hr.data.chat

import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.chat.list.EmployListResponseDto
import io.ktor.client.HttpClient

class KtorChatRemoteRepository(
    private val httpClient: HttpClient
) : ChatRemoteRepository {
    override suspend fun fetchEmployList(): ERPResult<EmployListResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                endPoint = EndPoint.EMPLOY_LIST_ENDPOINT
            )
        }
    }
}