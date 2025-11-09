package com.gurkha.hr.model.attendanceRequestScreen

import com.gurkha.hr.components.date.DateData
import com.gurkha.hr.domain.attendance.clockStatusEnum.ClockStatus
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import com.gurkha.model.leave.ui.AssigneeUi
import org.jetbrains.compose.resources.StringResource

data class AttendanceRequestState(
    val date: DateData? = null,
    val dateError: StringResource? = null,

    val clockInTime: String? = null,
    val clockInOutError: StringResource? = null,

    val clockOutTime: String? = null,
    val clockOutTimeError: StringResource? = null,

    val reason: String? = null,
    val reasonError: StringResource? = null,

    val isAssigneeLoading: Boolean = false,
    val isAssigneeFetchingError: Boolean = false,
    val assignee: AssigneeUi? = null,
    val assigneeList: List<AssigneeUi>? = emptyList(),
    val assigneeError: StringResource? = null,

    val isRequestingAttendance: Boolean = false,
    val radioOptions: List<ClockStatus> = ClockStatus.list,
    val selectedOption: ClockStatus = radioOptions.first(),
    val attendanceRequestData: AttendanceRequestData? = null
)

