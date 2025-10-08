package com.gurkha.hr.model.attendanceScreen

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.model.attendanceScreen.TabItemsEnums
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

    val tabItemsList: List<TabItemsEnums> = TabItemsEnums.Companion.list,

    val selectedTab: TabItemsEnums = TabItemsEnums.PENDING,
    val leaveRequestDataJson: String? = null,

    val isRequestingAttendance : Boolean = false,

    val attendanceGridOptions: List<AttendanceItem> = listOf(
        AttendanceItem(
            title = SharedRes.Strings.missed_attendance,
            days = 13,
            color = Color(0xFF81D4FA),
            backGroundColor = Color(0xFFE1F5FE)
        ),
        AttendanceItem(
            title = SharedRes.Strings.attendance_approved,
            days = 2,
            color = Color(0xFFA5D6A7),
            backGroundColor = Color(0xFFE8F5E9)
        ),
        AttendanceItem(
            title = SharedRes.Strings.attendance_pending,
            days = 4,
            color = Color(0xFFC5E1A5),
            backGroundColor = Color(0xFFF1F8E9)
        ),
        AttendanceItem(
            title = SharedRes.Strings.attendance_cancelled,
            days = 5,
            color = Color(0xFFEF9A9A),
            backGroundColor = Color(0xFFFFEBEE)
        ),
    )
)

data class AttendanceItem(
    val title : StringResource,
    val days : Int,
    val color: Color,
    val backGroundColor: Color
)

data class AttendanceTabItem(
    val isLoading: Boolean = false,
    val result: List<AttendanceStatusData> = emptyList(),
)


