package com.gurkha.hr.home.Model

data class HomeScreenState(
    val calendarItem: List<CalendarItem> = generateCalendarDays(),
    val requestRow1: List<AttendanceItem> = attendanceList,
    val requestRow2: List<AttendanceItem> = attendanceList2,
    val userName: String = "Suneel Shrestha",
    val position: String = "App Developer",
    val avatar: String = "",
    val isLoading : Boolean = false

)