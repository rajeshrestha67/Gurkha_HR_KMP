package com.gurkha.hr.leave.leaveRequestPage

import com.gurkha.hr.res.SharedRes
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class DropDownValue(
    val title : StringResource
)

val LeaveDurationList = listOf<DropDownValue>(
    DropDownValue(title = SharedRes.Strings.fullDay),
    DropDownValue(title = SharedRes.Strings.halfMorning),
    DropDownValue(title = SharedRes.Strings.halfAfternoon),
)

val LeaveTypeList = listOf<DropDownValue>(
    DropDownValue(title = SharedRes.Strings.sickLeave),
    DropDownValue(title = SharedRes.Strings.annualLeave),
    DropDownValue(title = SharedRes.Strings.maternityLeave),
    DropDownValue(title = SharedRes.Strings.mourningLeave),
)

//date formater
@OptIn(ExperimentalTime::class)
fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd"): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    return when(pattern) {
        "MM/dd/yyyy" -> "${localDate.monthNumber.toString().padStart(2, '0')}/${localDate.dayOfMonth.toString().padStart(2, '0')}/${localDate.year}"
        else -> "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}-${localDate.dayOfMonth.toString().padStart(2, '0')}"
    }
}
