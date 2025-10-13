package com.gurkha.hr.networkhelper

import com.gurkha.model.timeandAttendance.TimeAttendanceReportResponseDTO

object EndPoint {
    const val LOGIN_END_POINT = "public/authenticate"
    const val CHANGE_PASSWORD_END_POINT = "/api/user/password-reset"
    const val FETCH_ATTENDANCE_END_POINT = "api/attendance/employee/report"
    const val CURRENT_USER_DETAIL_END_POINT = "api/user/current-user"
    const val UPCOMING_BIRTHDAY_END_POINT = "api/birthday/upcomingBirthdays"
    const val UPCOMING_WORK_ANNIVERSARY_END_POINT = "api/birthday/allWorkingAnniversaries"
    const val ATTENDANCE_STATUS_REPORT_ENDPOINT = "api/attendance/attendanceStatus"
    const val LEAVE_STATUS_REPORT_ENDPOINT = "api/leave-request/employee/report"
    const val LEAVE_ASSIGNEE_END_POINT = "api/user/fetch-assignee"
    const val LEAVE_TYPE_END_POINT = "api/leave-type"
    const val LEAVE_REQUEST_END_POINT = "api/leave-request"
    const val EMPLOY_LIST_ENDPOINT = "api/chat/employee"
    const val CHAT_MESSAGE_ENDPOINT = "api/external-chat/messages"
    const val ALLOCATED_LEAVE_ENDPOINT = "api/employee-leave/136"
    const val TIME_AND_ATTENDANCE_ENDPOINT = "api/attendance/employee/report"

    const val ATTENDANCE_REQUEST_END_POINT = "api/attendance/request"
    const val LEAVE_SUMMARY_END_POINT = "api/leave-request/overall-leave-detail"
    const val ATTENDANCE_SUMMARY_END_POINT = "api/attendance/overall-count"
    const val ALL_NOTES_END_POINT = "api/note/all"
}