package com.gurkha.hr.leave.model.leave

interface LeaveScreenAction {
    data class OnStatusChange(val status: LeaveStatusEnum) : LeaveScreenAction
    data class UpdateRequestData(val json: String?) : LeaveScreenAction
    data object OnRefresh: LeaveScreenAction
}