package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.domain.chat.mapper.toChatItem
import com.gurkha.hr.domain.chat.model.ChatItem
import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class ChatListUseCase(
    private val chatRemoteRepository: ChatRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<ChatItem>, DataError> {
        return chatRemoteRepository.fetchEmployList().map { employListResponseDto ->
            employListResponseDto.detail?.map { it.toChatItem() }?.sortedBy { it.sortOrder }
                ?: emptyList()
        }
    }
}