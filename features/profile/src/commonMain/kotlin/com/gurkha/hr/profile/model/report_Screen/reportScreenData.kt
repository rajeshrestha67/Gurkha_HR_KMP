package com.gurkha.hr.profile.model.report_Screen


import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource


enum class Heading(val title: StringResource) {
    MonthlyAttendance(SharedRes.Strings.monthlyAttendance),
    AttendanceSummary(SharedRes.Strings.attendanceSummary);

    companion object {
        val list = entries.toList()
    }
}
