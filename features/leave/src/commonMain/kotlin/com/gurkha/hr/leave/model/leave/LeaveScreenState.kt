package com.gurkha.hr.leave.model.leave

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class LeaveScreenState(
    val pendingTapItem: LeaveTapItem = LeaveTapItem(),
    val approvedTapItem: LeaveTapItem = LeaveTapItem(),
    val rejectedTapItem: LeaveTapItem = LeaveTapItem(),
    val currentTapItem: LeaveTapItem = LeaveTapItem(),
    val fromDate: String = "",
    val toDate: String = "",
    val leaveStatus: LeaveStatusEnum = LeaveStatusEnum.PENDING,
    val employeeName: String = "",
    val isSelf: String = "",
    val leaveRequestDataJson: String? = null,

    val isRequestingLeave: Boolean = false,
    val leaveRequestError: Boolean = false,

    val leaveItemsList: List<LeaveItem> = listOf(
        LeaveItem(
            title = SharedRes.Strings.leave_balance,
            days = "20",
            color = Color(0xFF81D4FA),
            backGroundColor = Color(0xFFE1F5FE)
        ),
        LeaveItem(
            title = SharedRes.Strings.leave_approved,
            days = "2",
            color = Color(0xFFA5D6A7),
            backGroundColor = Color(0xFFE8F5E9)
        ),
        LeaveItem(
            title = SharedRes.Strings.leave_pending,
            days = "5",
            color = Color(0xFFC5E1A5),
            backGroundColor = Color(0xFFF1F8E9)
        ),
        LeaveItem(
            title = SharedRes.Strings.leave_cancelled,
            days = "7",
            color = Color(0xFFEF9A9A),
            backGroundColor = Color(0xFFFFEBEE)
        ),
    )
)
data class LeaveTapItem(
    val isLoading: Boolean = false,
    val result: List<LeaveReportData> = emptyList(),
)

data class LeaveItem(
    val title: StringResource,
    val days: String,
    val color: Color,
    val backGroundColor: Color
)
