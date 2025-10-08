package com.gurkha.hr.profile.model.time_and_attendance_screen

import com.gurkha.hr.domain.timeAndAttendance.model.TimeAndAttendanceData

data class TimeAndAttendanceState(
    val isLoading: Boolean = false,
    val timeAndAttendanceList: List<TimeAndAttendanceData> = emptyList(),


    )