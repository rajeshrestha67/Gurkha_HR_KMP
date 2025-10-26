package com.gurkha.hr.domain.leave.leaveSummary.mappper

import com.gurkha.hr.domain.leave.leaveSummary.model.LeaveSummaryData
import com.gurkha.model.leave.leaveSummary.LeaveSummaryResponseDto

fun LeaveSummaryResponseDto.toData(): LeaveSummaryData {
    return LeaveSummaryData(
        pendingCount = detail?.pendingCount ?: 0,
        approvedCount = detail?.approvedCount ?: 0,
        rejectedCount = detail?.rejectedCount ?: 0,
        remainingLeaveCount = detail?.remainingLeaveCount ?: 0.0
    )
}