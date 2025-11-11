package com.gurkha.hr.model.attendanceScreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class AttendanceScreenState(
    val attendanceList: List<AttendanceData> = emptyList(),
    val isLoading: Boolean = false,
    val pendingTapItem: AttendanceTabItem = AttendanceTabItem(),
    val approvedTapItem: AttendanceTabItem = AttendanceTabItem(),
    val rejectedTapItem: AttendanceTabItem = AttendanceTabItem(),
    val currentTapItem: AttendanceTabItem = AttendanceTabItem(),
    val employeeName: String = "",
    val isSelf: String = "Y",

    val attendanceStatus: TabItemsEnums = TabItemsEnums.PENDING,


    val tabItemsList: List<TabItemsEnums> = TabItemsEnums.Companion.list,

    val selectedTab: TabItemsEnums = TabItemsEnums.PENDING,

    val isRequestingAttendance: Boolean = false,

    val isFetchingAttendanceSummary : Boolean  = false,

    val attendanceGridOptions: List<AttendanceItem> = listOf(
        AttendanceItem(
            title = SharedRes.Strings.missed_attendance,
            days = "-",
            enum = null
        ),
        AttendanceItem(
            title = SharedRes.Strings.attendance_approved,
            days = "-",
            enum = TabItemsEnums.APPROVED
        ),
        AttendanceItem(
            title = SharedRes.Strings.attendance_pending,
            days = "-",
            enum = TabItemsEnums.PENDING

        ),
        AttendanceItem(
            title = SharedRes.Strings.attendance_cancelled,
            days = "-",
            enum = TabItemsEnums.REJECTED
        ),
    ),

    val isRefreshing : Boolean = false
)

data class AttendanceItem(
    val title: StringResource,
    val days: String,
    val enum: TabItemsEnums?
)

data class AttendanceTabItem(
    val isLoading: Boolean = false,
    val result: List<AttendanceStatusData> = emptyList(),
)

val AttendanceItem.backgroundColor: Color
    @Composable get() =
        when(title){
            SharedRes.Strings.missed_attendance -> MaterialTheme.erpColors.box1BackgroundColor
            SharedRes.Strings.attendance_approved -> MaterialTheme.erpColors.box2BackgroundColor
            SharedRes.Strings.attendance_pending -> MaterialTheme.erpColors.box3BackgroundColor
            SharedRes.Strings.attendance_cancelled -> MaterialTheme.erpColors.box4BackgroundColor
            else -> MaterialTheme.erpColors.box1BackgroundColor
        }

val AttendanceItem.outlineColor: Color
    @Composable get() =
        when(title){
            SharedRes.Strings.missed_attendance -> MaterialTheme.erpColors.box1OutlineColor
            SharedRes.Strings.attendance_approved -> MaterialTheme.erpColors.box2OutlineColor
            SharedRes.Strings.attendance_pending -> MaterialTheme.erpColors.box3OutlineColor
            SharedRes.Strings.attendance_cancelled -> MaterialTheme.erpColors.box4OutlineColor
            else -> MaterialTheme.erpColors.box1OutlineColor
        }

