package com.gurkha.hr.domain.allocatedLeave.model

data class AllocatedLeaveData(
    val leaveType: String,
    val totalDays: Double,
    val leaveTaken: Double,
    val remainingLeave: Double,
)