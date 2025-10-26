package com.gurkha.model.leave.leaveSummary

import kotlinx.serialization.Serializable

@Serializable
data class LeaveSummaryResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: LeaveDetailDto? = null,
    val success: Boolean? = null
)

@Serializable
data class LeaveDetailDto(
    val pendingCount: Int? = null,
    val approvedCount: Int? = null,
    val rejectedCount: Int? = null,
    val remainingLeaveCount: Double? = null
)