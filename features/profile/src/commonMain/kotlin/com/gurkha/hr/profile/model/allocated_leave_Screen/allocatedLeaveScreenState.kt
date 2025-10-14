package com.gurkha.hr.profile.model.allocated_leave_Screen

import com.gurkha.hr.domain.allocatedLeave.model.AllocatedLeaveData

data class AllocatedLeaveState(
    val isLoading: Boolean = false,
    val leaveSummaryList: List<AllocatedLeaveData> = emptyList(),
)
