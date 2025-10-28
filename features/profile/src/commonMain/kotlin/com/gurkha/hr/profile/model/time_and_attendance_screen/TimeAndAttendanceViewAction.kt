package com.gurkha.hr.profile.model.time_and_attendance_screen

import com.gurkha.hr.components.date.DateData


sealed interface TimeAndAttendanceViewAction {
    data class fromDate(val date: DateData) : TimeAndAttendanceViewAction
    data class toDate(val date: DateData) : TimeAndAttendanceViewAction

    data class Submit(val employeeId: Int) : TimeAndAttendanceViewAction

    data object OnRefresh: TimeAndAttendanceViewAction
}