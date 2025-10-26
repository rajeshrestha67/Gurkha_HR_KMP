package com.gurkha.hr.profile.model.allocated_leave_Screen

import org.jetbrains.compose.resources.StringResource

data class LeaveTypeData(
    val title: StringResource,
    val totalDays: String,
    val leaveTaken: String,
    val remainingLeave: String
)

