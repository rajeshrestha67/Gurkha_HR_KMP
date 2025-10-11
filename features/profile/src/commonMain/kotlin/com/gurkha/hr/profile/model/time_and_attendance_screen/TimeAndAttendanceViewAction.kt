package com.gurkha.hr.profile.model.time_and_attendance_screen

import com.gurkha.hr.components.textField.DateData
import com.gurkha.hr.domain.timeAndAttendance.model.TimeAndAttendanceData

sealed interface TimeAndAttendanceViewAction {
    data class fromDate(val date: DateData) : TimeAndAttendanceViewAction
    data class toDate(val date: DateData): TimeAndAttendanceViewAction

    data object Submit : TimeAndAttendanceViewAction
}