package com.gurkha.hr.domain.chat.mapper

import com.gurkha.hr.domain.chat.model.ChatMessageContent
import com.gurkha.hr.domain.chat.model.ChatMessageData
import com.gurkha.hr.domain.chat.model.ChatMessageMetaData
import com.gurkha.model.chat.list.ChatMessageContentResponseDto
import com.gurkha.model.chat.list.ChatMessageDetailResponseDto
import com.gurkha.model.chat.list.ChatMessageResponseDto
import com.gurkha.model.user_data.UserData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun ChatMessageResponseDto.toChatMessageData(userData: UserData): ChatMessageData {
    return ChatMessageData(
        metaData = detail?.toMetaData(),
        messages = detail?.content?.map { it.toChatMessageContent(userData = userData) } ?: listOf()
    )
}

fun ChatMessageDetailResponseDto.toMetaData(): ChatMessageMetaData {
    return ChatMessageMetaData(
        currentPage = number ?: 0,
        totalPages = totalPages ?: 1
    )
}

@OptIn(ExperimentalTime::class)
fun ChatMessageContentResponseDto.toChatMessageContent(userData: UserData): ChatMessageContent {
    return ChatMessageContent(
        message = content ?: "",
        date = createdDate.toInstant(),
        fromMe = userData.fullName.equals(fromUser, ignoreCase = true)
    )
}

@OptIn(ExperimentalTime::class)
fun String?.toInstant(): Instant? {
    if (this.isNullOrEmpty()) return null

    var normalized = this.trim()

    // normalize fractions to nanoseconds
    if (normalized.contains(".")) {
        val parts = normalized.split(".")
        val fraction = parts[1].padEnd(9, '0').take(9) // always nanoseconds
        normalized = "${parts[0]}.${fraction}"
    }

    // ensure UTC offset
    if (!normalized.endsWith("Z") && !normalized.contains("+")) {
        normalized += "Z"
    }

    return Instant.parse(normalized)
}

@OptIn(ExperimentalTime::class)
fun Instant?.toChatFormattedData(): String {
    return this?.let {
        try {
            val localDateTime = this.toLocalDateTime(TimeZone.UTC)
            localDateTime.format(
                LocalDateTime.Format {
                    monthName(MonthNames.ENGLISH_FULL)
                    chars(" ")
                    day(padding = Padding.ZERO)
                    chars(", ")
                    year()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid date: $this"
        }
    } ?: "Invalid date: $this"
}

@OptIn(ExperimentalTime::class)
fun Instant?.toChatFormattedTime(): String {
    return this?.let {
        try {
            val localDateTime = this.toLocalDateTime(TimeZone.UTC)
            localDateTime.format(
                LocalDateTime.Format {
                    hour()
                    chars(":")
                    minute()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid date: $this"
        }
    } ?: "Invalid date: $this"
}

@OptIn(ExperimentalTime::class)
fun getCurrentDataAndTime(): Pair<String, String> {
    val instant: Instant = Clock.System.now()
    return try {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val date = localDateTime.format(
            LocalDateTime.Format {
                monthName(MonthNames.ENGLISH_FULL)
                chars(" ")
                day(padding = Padding.ZERO)
                chars(", ")
                year()
            }
        )
        val time = localDateTime.format(
            LocalDateTime.Format {
                hour()
                chars(":")
                minute()
            }
        )
        Pair(date, time)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair("Invalid date: $instant", "Invalid date: $instant")
    }
}
