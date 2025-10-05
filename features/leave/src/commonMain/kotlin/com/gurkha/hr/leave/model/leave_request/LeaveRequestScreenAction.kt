package com.gurkha.hr.leave.model.leave_request

import com.gurkha.hr.components.textField.DateData
import com.gurkha.model.leave_request.LeaveRequestData
import org.jetbrains.compose.resources.StringResource

sealed interface LeaveRequestScreenAction {
    data class OnStartDateChange(val date: DateData) : LeaveRequestScreenAction
    data class OnEndDateChange(val date: DateData) : LeaveRequestScreenAction
    data class OnLeaveDurationChange(val leaveDuration: String) : LeaveRequestScreenAction
    data class OnLeaveTypeChange(val leaveType: String) : LeaveRequestScreenAction
    data class OnAssigneeChange(val assignee: String) : LeaveRequestScreenAction
    data class OnReasonChange(val reason: String) : LeaveRequestScreenAction
    data class OnReasonError(val error: StringResource?) : LeaveRequestScreenAction
    data class OnAssigneeError(val error: StringResource?) : LeaveRequestScreenAction

    data object OnRefetchAssignee : LeaveRequestScreenAction
    data object OnRefetchLeaveType : LeaveRequestScreenAction

    data class UpdateLeaveRequestData(val data: LeaveRequestData?) : LeaveRequestScreenAction
    data object Submit : LeaveRequestScreenAction
}