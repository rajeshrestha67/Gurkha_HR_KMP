package com.gurkha.hr.model.attendanceRequestScreen

import com.gurkha.hr.components.date.DateData
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import com.gurkha.model.leave.ui.AssigneeUi

interface AttendanceRequestAction {
    data class OnAssigneeChange(val assignee: AssigneeUi) : AttendanceRequestAction
    data class OnDateChange(val date: DateData) : AttendanceRequestAction
    data class OnClockInTimeChange(val clockInTime: String) : AttendanceRequestAction
    data class OnClockOutTimeChange(val clockOutTime: String) : AttendanceRequestAction
    data class OnReasonChange(val reason: String) : AttendanceRequestAction

    data object OnSubmit : AttendanceRequestAction

    data class OnUpdateAttendanceRequestData(val data: AttendanceRequestData) :
        AttendanceRequestAction
}