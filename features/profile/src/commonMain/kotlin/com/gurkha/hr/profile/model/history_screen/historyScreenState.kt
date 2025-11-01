package com.gurkha.hr.profile.model.history_screen


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.domain.history.model.HistoryData
import org.jetbrains.compose.resources.StringResource

data class HistoryState(
    val isLoading: Boolean = false,
    val monthValue: Int = 6,
    val year: Int = 2082,
    val monthDisplay: String = "Asoj",
    val historySummaryList: List<HistoryDataUI> = emptyList(),
    val employeeId: Int? = null,

    val endYearError: StringResource? = null,
    val endMonthError: StringResource? = null,

    val isRefreshing: Boolean = false
)

data class HistoryDataUI(
    val date: String,
    val day: String,
    val clockInTime: String,
    val clockOutTime: String,
    val lateInTime: String,
    val earlyOutTime: String,
    val assigneeName: String,
    val remarks: String,
    val response: String,
    val assigneeStatus: String,
    val leaveRequestStatus: String,
    val leaveApproverRemarks: String,
    val leaveDuration: String,
    val attendanceStatus: String,
    val isPresent: Boolean,
    val attendanceTextColor: AttendanceTextColor
)

fun HistoryData.toUI(): HistoryDataUI {
    return HistoryDataUI(
        date = date,
        day = day,
        clockInTime = clockInTime,
        clockOutTime = clockOutTime,
        lateInTime = lateInTime,
        earlyOutTime = earlyOutTime,
        assigneeName = assigneeName,
        remarks = remarks,
        response = response,
        assigneeStatus = assigneeStatus,
        leaveRequestStatus = leaveRequestStatus,
        leaveApproverRemarks = leaveApproverRemarks,
        leaveDuration = leaveDuration,
        attendanceStatus = attendanceStatus,
        isPresent = attendanceStatus.equals("P", ignoreCase = true),
        attendanceTextColor = AttendanceTextColor.get(attendanceStatus)
    )
}

enum class AttendanceTextColor(val value: String) {

    Present("P"),
    Absent("A"),
    Holiday("H");

    companion object {
        private val typeMap =
            enumValues<AttendanceTextColor>().associateBy { it.value }

        fun get(typeName: String): AttendanceTextColor =
            AttendanceTextColor.typeMap[typeName] ?: Present

        val list: List<AttendanceTextColor>
            get() = AttendanceTextColor.entries.toList().map { it }
    }

    val textColor: Color
        @Composable get() =
            when (this) {
                Present -> MaterialTheme.erpColors.lightGreenColor
                Absent -> MaterialTheme.erpColors.lightRedColor
                Holiday -> MaterialTheme.erpColors.holidayBlueColor
            }


}