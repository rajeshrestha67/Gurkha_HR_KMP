package com.gurkha.hr.home.Model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class CalenderItem(
    val date: String,
    val day: String,
    val active: Boolean
)

val calenderList = listOf(
    CalenderItem("10", "Mon", false),
    CalenderItem("11", "Tue", false),
    CalenderItem("12", "Wed", true),
    CalenderItem("13", "Thu", false),
    CalenderItem("14", "Fri", false),
)

data class AttendanceItem(
    val icon: ImageVector,
    val title: String,
    val time: String,
    val status: String
)

val attendanceList = listOf(
    AttendanceItem(Icons.Filled.Home, "Check In", "10.20 AM", "On Time"),
    AttendanceItem(Icons.Filled.Home, "Check Out", "5.30 AM", "Go Home"),
)
val attendanceList2 = listOf(
    AttendanceItem(Icons.Filled.Home, "Break Time", "1.20 AM", "Avg Time 30 min"),
    AttendanceItem(Icons.Filled.Home, "Total Days", "28", "Working Days"),
)