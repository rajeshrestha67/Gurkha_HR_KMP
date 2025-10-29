package com.gurkha.hr.domain.attendance.attendanceReport.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.erpColors

data class AttendanceData(
    val workingHrs: String,
    val clockInTime: String? = null,
    val clockOutTime: String? = null,
    val date: String,
    val day: String,
    val status: AttendanceStatus,
    val onLeave: Boolean,
    val isHoliday: Boolean,
    val isLate: Boolean,
    val isEarlyOut: Boolean,
    val employeeId: Int
)


enum class AttendanceStatus(val value: String) {
    PRESENT("Present"),
    ABSENT("Absent"),
    HOLIDAY("Holiday"),
    ON_LEAVE("ON_LEAVE"),
    HALF_LEAVE("HALF_LEAVE");

    companion object {
        private val typeMap =
            enumValues<AttendanceStatus>().associateBy { it.value.lowercase() }

        fun get(typeName: String): AttendanceStatus =
            AttendanceStatus.typeMap[typeName.trim().lowercase()] ?: PRESENT

        val list: List<AttendanceStatus>
            get() = AttendanceStatus.entries.toList().map { it }

    }


    val color: Color
        @Composable get() = when (this) {
            PRESENT -> MaterialTheme.colorScheme.primary
            ABSENT -> MaterialTheme.colorScheme.error
            HOLIDAY -> MaterialTheme.erpColors.attendanceHoliday
            else -> Color.Transparent
        }
}