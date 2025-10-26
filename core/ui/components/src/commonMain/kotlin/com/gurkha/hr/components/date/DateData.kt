package com.gurkha.hr.components.date

import com.gurkha.hr.date.DateConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class DateData(
    val displayValueAD: String,
    val displayValueBS: String,
    val actualValue: Long
) {
    companion object {

        // Create from formatted string
        @OptIn(ExperimentalTime::class)
        fun fromDisplayAD(
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
            val dateInBS = DateConverter.adToBs(
                year = localDate.year,
                month = localDate.month.number,
                day = localDate.day
            )
            val displayValueBS = "${dateInBS.year}-${
                dateInBS.month.toString().padStart(2, '0')
            }-${dateInBS.day.toString().padStart(2, '0')}"

            val millis = localDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
            return DateData(
                displayValueAD = displayValue,
                actualValue = millis,
                displayValueBS = displayValueBS
            )
        }

        @OptIn(ExperimentalTime::class)
        fun fromDisplayBS(
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
            val dateInAD = DateConverter.bsToAd(
                year = localDate.year,
                month = localDate.month.number,
                day = localDate.day
            )
            val displayValueAD = "${dateInAD.year}-${
                dateInAD.month.toString().padStart(2, '0')
            }-${dateInAD.day.toString().padStart(2, '0')}"

            val millis = localDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
            return DateData(
                displayValueAD = displayValueAD,
                actualValue = millis,
                displayValueBS = displayValue
            )
        }
    }
}