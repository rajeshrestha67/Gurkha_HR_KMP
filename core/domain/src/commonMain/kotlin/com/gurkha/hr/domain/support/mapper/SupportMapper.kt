package com.gurkha.hr.domain.support.mapper

import com.gurkha.hr.domain.support.model.SupportListData
import com.gurkha.model.support.SupportResponseDto

fun SupportResponseDto.toData(): List<SupportListData>{
    return detail?.map {
        SupportListData(
            phoneNumber = it.phoneNumber ?: "",
            chatId = it.chatId ?: "",
            coopName = it.coopName ?: ""
        )
    } ?: emptyList()
}