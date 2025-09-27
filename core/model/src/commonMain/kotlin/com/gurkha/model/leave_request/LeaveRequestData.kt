package com.gurkha.model.leave_request

import kotlinx.serialization.Serializable

@Serializable
data class LeaveRequestData(
    val startDate: String,
    val endDate: String,
    val leaveDuration: String,
    val leaveType: String,
    val reason: String
)
