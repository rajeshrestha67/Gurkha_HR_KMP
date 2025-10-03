package com.gurkha.hr.domain.chat.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.chat.list.ChatMessageResponseDto
import com.gurkha.model.chat.list.EmployListResponseDto

interface ChatRemoteRepository {

    suspend fun fetchEmployList(): ERPResult<EmployListResponseDto, DataError>

    suspend fun fetchChatMessage(
        chatId: String,
        page: Int,
        size: Int
    ): ERPResult<ChatMessageResponseDto, DataError>


}