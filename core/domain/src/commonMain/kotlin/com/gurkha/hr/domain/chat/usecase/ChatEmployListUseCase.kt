package com.gurkha.hr.domain.chat.usecase

import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.hr.domain.chat.mapper.toChatItem
import com.gurkha.hr.domain.chat.model.EmployChatItem
import com.gurkha.hr.domain.chat.repository.ChatRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class ChatEmployListUseCase(
    private val chatRemoteRepository: ChatRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<List<EmployChatItem>, DataError> {
        return chatRemoteRepository.fetchEmployList().map { employListResponseDto ->
            employListResponseDto.detail?.map { it.toChatItem() }?.sortedBy { it.sortOrder }
                ?: emptyList()
        }
    }
}