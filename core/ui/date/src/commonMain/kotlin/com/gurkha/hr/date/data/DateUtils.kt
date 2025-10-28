package com.gurkha.hr.date.data

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object DateUtils {
    fun getCurrentTime(): String{
        @OptIn(ExperimentalTime::class)
        val now = Clock.System.now()
        @OptIn(ExperimentalTime::class)
        val localTime = now.toLocalDateTime(TimeZone.currentSystemDefault()).time

        val finalTime = localTime.hour.toString() + ":" + localTime.minute.toString()
        return finalTime
    }
}