package com.gurkha.hr.domain.support.mapper

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.chat.model.EmployChatItem
import com.gurkha.hr.domain.support.model.SupportListData
import com.gurkha.model.support.SupportResponseDto
import kotlin.random.Random

fun SupportResponseDto.toData(): List<SupportListData>{
    return detail?.map {
        SupportListData(
            phoneNumber = it.phoneNumber ?: "",
            chatId = it.chatId ?: "",
            coopName = it.coopName ?: ""
        )
    } ?: emptyList()
}



fun List<SupportListData>.toChatList(): List<EmployChatItem>{
    return this.map {
        EmployChatItem(
            chatId = it.chatId,
            employeeName = it.coopName,
            phoneNumber = it.phoneNumber,
            employeeId = (1..100000).random().toLong(),
            branchName = it.phoneNumber,
            profileImageUrl = null,
            lastMessage = "",
            lastMessageSendUser = "",
            hasUnReadMessage = "",
            sortOrder = (1..100000).random(),
            nameInitials = it.coopName.extractInitials(),
            backgroundColor = randomLightColor(),
            isOnline = false,
        )
    }
}

private fun randomLightColor(): Color {
    val rnd = Random.Default
    // Ensure values are closer to 255 (light colors)
    val r = 150 + rnd.nextInt(106) // 150–255
    val g = 150 + rnd.nextInt(106)
    val b = 150 + rnd.nextInt(106)
    return Color(r, g, b)
}