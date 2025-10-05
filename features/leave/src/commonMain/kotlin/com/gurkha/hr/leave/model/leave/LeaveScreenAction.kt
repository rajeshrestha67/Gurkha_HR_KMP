package com.gurkha.hr.leave.model.leave

interface LeaveScreenAction {
    data class OnStatusChange(val status: LeaveStatusEnum) : LeaveScreenAction
    data class UpdateRequestData(val json: String?) : LeaveScreenAction

    data class OnLeaveRequest(
        val endDate: String,
        val leaveDuration: String,
        val leaveTypeId: Int,
        val reason: String,
        val startDate: String,
        val assigneeId: Int
    ): LeaveScreenAction
}