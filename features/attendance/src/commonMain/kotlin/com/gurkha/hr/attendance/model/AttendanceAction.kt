package com.gurkha.hr.attendance.model

interface AttendanceAction {
    data class OnStatusChange(val status: TabItemsEnums) : AttendanceAction
}