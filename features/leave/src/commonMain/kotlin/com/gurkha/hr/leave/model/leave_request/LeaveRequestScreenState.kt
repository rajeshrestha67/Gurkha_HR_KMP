package com.gurkha.hr.leave.model.leave_request

import com.gurkha.hr.components.textField.DateData
import com.gurkha.model.leave.leave_request.LeaveRequestData
import com.gurkha.model.leave.ui.LeaveAssigneeUi
import com.gurkha.model.leave.ui.LeaveDurationUi
import com.gurkha.model.leave.ui.LeaveTypeUi
import org.jetbrains.compose.resources.StringResource


data class LeaveRequestScreenState(
    val startDate: DateData? = null,
    val endDate: DateData? = null,
    val leaveDuration: LeaveDurationUi? = null,
    val leaveType: LeaveTypeUi? = null,
    val assignee: LeaveAssigneeUi? = null,
    val reason: String = "",

    val startDateError: StringResource? = null,
    val endDateError: StringResource? = null,
    val leaveDurationError: StringResource? = null,
    val leaveTypeError: StringResource? = null,
    val reasonError: StringResource? = null,
    val assigneeError: StringResource? = null,

    val leaveRequestData: LeaveRequestData? = null,

    val isAssigneeLoading: Boolean = false,
    val isLeaveTypeLoading: Boolean = false,

    val isAssigneeFetchingError : Boolean = false,
    val isLeaveTypeFetchingError : Boolean = false,

    val leaveTypeList : List<LeaveTypeUi>? = null,
    val leaveAssigneeList : List<LeaveAssigneeUi>? = null,
    val leaveDurationList: List<LeaveDurationUi> = listOf(
        LeaveDurationUi(
            name = "Full Day",
            value = "Full Day"
        ),
        LeaveDurationUi(
            name = "Half Day Morning",
            value = "Half Day Morning"
        ),
        LeaveDurationUi(
            name = "Half Day Afternoon",
            value = "Half Day Afternoon"
        ),
    )
)
