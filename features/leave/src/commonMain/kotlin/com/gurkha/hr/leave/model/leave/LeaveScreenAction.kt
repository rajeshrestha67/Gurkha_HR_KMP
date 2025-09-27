package com.gurkha.hr.leave.model.leave

interface LeaveScreenAction {
    data class OnStatusChange(val status: AttendanceStatusEnum) : LeaveScreenAction
    data class UpdateRequestData(val json: String?) : LeaveScreenAction
}