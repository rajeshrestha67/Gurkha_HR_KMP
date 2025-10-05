package com.gurkha.hr.leave.model.leave_request

import com.gurkha.hr.components.textField.DateData
import com.gurkha.hr.domain.leaveAssignee.model.LeaveAssigneeData
import com.gurkha.hr.domain.leaveType.model.LeaveTypeData
import com.gurkha.model.leave_request.LeaveRequestData
import org.jetbrains.compose.resources.StringResource

data class LeaveRequestScreenState(
    val startDate: DateData? = null,
    val endDate: DateData? = null,
    val leaveDuration: String = "",
    val leaveType: String = "",
    val reason: String = "",
    val assignee: String = "",

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

    val leaveTypeList : List<LeaveTypeData>? = null,
    val leaveAssigneeList : List<LeaveAssigneeData>? = null
)
