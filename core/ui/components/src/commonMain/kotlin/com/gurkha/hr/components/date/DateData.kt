package com.gurkha.hr.components.date

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class DateData(
    val displayValue: String,
    val actualValue: Long
) {
    companion object {
        // Create from millis
        fun fromMillis(
            millis: Long,
            pattern: String = "yyyy-MM-dd"
        ): DateData {
            return DateData(
                displayValue = millis.toFormattedDate(pattern),
                actualValue = millis
            )
        }

        // Create from formatted string
        @OptIn(ExperimentalTime::class)
        fun fromDisplay(
            displayValue: String,
            pattern: String = "yyyy-MM-dd",
            timeZone: TimeZone = TimeZone.Companion.currentSystemDefault()
        ): DateData {
            val localDate = when (pattern) {
                "MM/dd/yyyy" -> {
                    val parts = displayValue.split("/")
                    LocalDate(
                        year = parts[2].toInt(),
                        month = parts[0].toInt(),
                        day = parts[1].toInt()
                    )
                }

                else -> {
                    val parts = displayValue.split("-")
                    LocalDate(
                        year = parts[0].toInt(),
                        month = parts[1].toInt(),
                        day = parts[2].toInt()
                    )
                }
            }
            val millis = localDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
            return DateData(displayValue = displayValue, actualValue = millis)
        }
    }
}