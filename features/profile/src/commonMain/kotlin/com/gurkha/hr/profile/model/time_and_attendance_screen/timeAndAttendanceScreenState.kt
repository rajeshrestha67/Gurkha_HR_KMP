package com.gurkha.hr.profile.model.time_and_attendance_screen


import com.gurkha.hr.components.date.DateData
import com.gurkha.hr.domain.timeAndAttendance.model.TimeAndAttendanceData
import org.jetbrains.compose.resources.StringResource

data class TimeAndAttendanceState(
    val isLoading: Boolean = false,
    val timeAndAttendanceList: List<TimeAndAttendanceData> = emptyList(),
    val fromDate: DateData? = null,
    val toDate: DateData? = null,


    val fromDateError: StringResource? = null,
    val toDateError: StringResource? = null

)