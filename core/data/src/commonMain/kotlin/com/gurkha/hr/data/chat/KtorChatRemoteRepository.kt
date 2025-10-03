package com.gurkha.hr.data.chat

import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.chat.list.ChatMessageResponseDto
import com.gurkha.model.chat.list.EmployListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

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

    override suspend fun fetchChatMessage(
        chatId: String,
        page: Int,
        size: Int
    ): ERPResult<ChatMessageResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                endPoint = EndPoint.CHAT_MESSAGE_ENDPOINT
            ) {
                parameter("chatId", chatId)
                parameter("page", page)
                parameter("size", size)
            }
        }
    }
}