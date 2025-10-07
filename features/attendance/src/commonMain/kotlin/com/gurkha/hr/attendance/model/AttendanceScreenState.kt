package com.gurkha.hr.attendance.model

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class AttendanceScreenState(
    val attendanceList: List<AttendanceData> = emptyList(),
    val isLoading: Boolean = false,

    val tabItemsList: List<TabItemsEnums> = TabItemsEnums.list,

    val selectedTab: TabItemsEnums = TabItemsEnums.PENDING,

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

