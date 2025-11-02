package com.gurkha.hr.leave.model.leave

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class LeaveScreenState(
    val pendingTapItem: LeaveTapItem = LeaveTapItem(),
    val approvedTapItem: LeaveTapItem = LeaveTapItem(),
    val rejectedTapItem: LeaveTapItem = LeaveTapItem(),
    val currentTapItem: LeaveTapItem = LeaveTapItem(),
    val tabItemsList: List<LeaveStatusEnum> = LeaveStatusEnum.list,
    val fromDate: String = "",
    val toDate: String = "",
    val leaveStatus: LeaveStatusEnum = LeaveStatusEnum.PENDING,
    val leaveRequestDataJson: String? = null,

    val isRequestingLeave: Boolean = false,
    val leaveRequestError: Boolean = false,


    val isLeaveSummaryLoading: Boolean = false,

    val isRefreshing: Boolean = false,

    val leaveItemsList: List<LeaveItem> = listOf(
        LeaveItem(
            title = SharedRes.Strings.leave_balance,
            days = "-",
        ),
        LeaveItem(
            title = SharedRes.Strings.leave_approved,
            days = "-",
        ),
        LeaveItem(
            title = SharedRes.Strings.leave_pending,
            days = "-",
        ),
        LeaveItem(
            title = SharedRes.Strings.leave_cancelled,
            days = "-",
        ),
    ),
)

data class LeaveTapItem(
    val isLoading: Boolean = false,
    val result: List<LeaveReportData> = emptyList(),
)

data class LeaveItem(
    val title: StringResource,
    val days: String?,
)

val LeaveItem.backgroundColor: Color
    @Composable get()=
        when(title){
            SharedRes.Strings.leave_balance -> MaterialTheme.erpColors.box1BackgroundColor
            SharedRes.Strings.leave_approved -> MaterialTheme.erpColors.box2BackgroundColor
            SharedRes.Strings.leave_pending -> MaterialTheme.erpColors.box3BackgroundColor
            SharedRes.Strings.leave_cancelled -> MaterialTheme.erpColors.box4BackgroundColor

            else -> MaterialTheme.erpColors.box1BackgroundColor
        }

val LeaveItem.outlineColor: Color
    @Composable get()=
        when(title){
            SharedRes.Strings.leave_balance -> MaterialTheme.erpColors.box1OutlineColor
            SharedRes.Strings.leave_approved -> MaterialTheme.erpColors.box2OutlineColor
            SharedRes.Strings.leave_pending -> MaterialTheme.erpColors.box3OutlineColor
            SharedRes.Strings.leave_cancelled -> MaterialTheme.erpColors.box4OutlineColor

            else ->MaterialTheme.erpColors.box1OutlineColor
        }
