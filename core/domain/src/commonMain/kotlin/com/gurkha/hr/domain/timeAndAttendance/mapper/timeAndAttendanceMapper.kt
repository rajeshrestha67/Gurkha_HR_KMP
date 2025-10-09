package com.gurkha.hr.domain.timeAndAttendance.mapper

import com.gurkha.hr.domain.attendance.model.AttendanceData
import com.gurkha.hr.domain.timeAndAttendance.model.TimeAndAttendanceData
import com.gurkha.model.timeandAttendance.TimeAttendanceReportResponseDTO


fun TimeAttendanceReportResponseDTO.toData(): List<TimeAndAttendanceData>{
    return detail?.map{
        TimeAndAttendanceData(
            date = it.created?:"",
            day = it.dayOfWeek?:"",
            clockInTime = it.clockInTime?:"",
            clockOutTime =it.clockOutTime?:"" ,
            status =it.attendanceStatus?:"",
            isPresent = it.onLeave?:false,
            isHoliday = it.holiday?:false,
            isLate = it.isLate?:false,
            isEarlyOut = it.isLeaveEarly?:false,


        )
    } ?: emptyList()}