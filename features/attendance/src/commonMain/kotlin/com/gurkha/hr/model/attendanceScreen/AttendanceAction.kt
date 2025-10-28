package com.gurkha.hr.model.attendanceScreen

import com.gurkha.hr.model.attendanceScreen.TabItemsEnums

interface AttendanceAction {
    data class OnStatusChange(val status: TabItemsEnums) : AttendanceAction

    data class OnUpdateAttendanceJsonData( val json : String?): AttendanceAction
    data object OnRefresh: AttendanceAction
}