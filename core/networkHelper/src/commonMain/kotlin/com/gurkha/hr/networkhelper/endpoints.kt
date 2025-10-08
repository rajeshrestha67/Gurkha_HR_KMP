package com.gurkha.hr.networkhelper

import com.gurkha.model.timeandAttendance.TimeAttendanceReportResponseDTO

object EndPoint{
    const val LOGIN_END_POINT = "public/authenticate"
    const val CHANGE_PASSWORD_END_POINT = "/api/user/password-reset"
    const val FETCH_ATTENDANCE_END_POINT ="api/attendance/employee/report"
    const val CURRENT_USER_DETAIL_END_POINT = "api/user/current-user"
    const val UPCOMING_BIRTHDAY_END_POINT = "api/birthday/upcomingBirthdays"
    const val UPCOMING_WORK_ANNIVERSARY_END_POINT = "api/birthday/allWorkingAnniversaries"
    const val ATTENDANCE_STATUS_REPORT_ENDPOINT = "api/attendance/attendanceStatus"
    const val ALLOCATED_LEAVE_ENDPOINT = "api/employee-leave/136"
    const val TIME_AND_ATTENDANCE_ENDPOINT = "api/attendance/employee/report"

}