package com.gurkha.hr.domain.chat.mapper


import androidx.compose.ui.graphics.Color
import com.gurkha.hr.domain.chat.model.ChatItem
import com.gurkha.model.chat.list.EmptyListItemResponseDto
import kotlin.random.Random

fun EmptyListItemResponseDto.toChatItem(): ChatItem {
    return ChatItem(
        employeeId = employeeId ?: 0L,
        chatId = chatId ?: "",
        branchName = branchName ?: "",
        employeeName = employeeName ?: "",
        profileImageUrl = profileImageUrl?.let { "https://mbank.gurkhahr.com/erp-images/$it" },
        lastMessage = lastMessage ?: "",
        lastMessageSendUser = lastMessageSendUser ?: "",
        hasUnReadMessage = hasUnReadMessage ?: "",
        sortOrder = sortOrder ?: 0,
        nameInitials = extractInitials(name = employeeName),
        backgroundColor = randomLightColor()
    )
}

private fun extractInitials(name: String?): String {
    if (name.isNullOrBlank()) return ""

    val nameParts = name.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }

    return when {
        nameParts.isEmpty() -> ""
        nameParts.size == 1 -> nameParts.first().first().uppercase()
        else -> {
            val first = nameParts.first().first().uppercase()
            val last = nameParts.last().first().uppercase()
            "$first$last"
        }
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
