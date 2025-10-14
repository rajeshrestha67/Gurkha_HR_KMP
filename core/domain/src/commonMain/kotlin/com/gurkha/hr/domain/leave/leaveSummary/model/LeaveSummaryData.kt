package com.gurkha.hr.domain.leave.leaveSummary.model

data class LeaveSummaryData(
    val pendingCount: Int,
    val approvedCount: Int,
    val rejectedCount: Int,
    val remainingLeaveCount: Double
)
