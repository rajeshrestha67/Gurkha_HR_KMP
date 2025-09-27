package com.gurkha.hr.leave.model.leave_request

import com.gurkha.hr.components.textField.DateData
import org.jetbrains.compose.resources.StringResource

sealed interface LeaveRequestScreenAction {
    data class OnStartDateChange(val date: DateData) : LeaveRequestScreenAction
    data class OnEndDateChange(val date: DateData) : LeaveRequestScreenAction
    data class OnLeaveDurationChange(val leaveDuration: String) : LeaveRequestScreenAction
    data class OnLeaveTypeChange(val leaveDuration: String) : LeaveRequestScreenAction
    data class OnReasonChange(val leaveDuration: String) : LeaveRequestScreenAction

    data class OnStartDateError(val error: StringResource?) : LeaveRequestScreenAction
    data class OnEndDateError(val error: StringResource?) : LeaveRequestScreenAction
    data class OnLeaveDurationError(val error: StringResource?) : LeaveRequestScreenAction
    data class OnLeaveTypeError(val error: StringResource?) : LeaveRequestScreenAction
    data class OnReasonError(val error: StringResource?) : LeaveRequestScreenAction
    data object Submit : LeaveRequestScreenAction
}