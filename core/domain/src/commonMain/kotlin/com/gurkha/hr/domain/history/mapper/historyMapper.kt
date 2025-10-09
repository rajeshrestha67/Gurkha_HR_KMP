//package com.gurkha.hr.domain.history.mapper
//
//import com.gurkha.hr.domain.history.model.HistoryData
//import com.gurkha.model.history.DailyAttendance
//import com.gurkha.model.history.HistoryResponseDTO
//
//fun HistoryResponseDTO.toData(): List<HistoryData>? {
//    return detail.map { attendanceDetail ->
//        attendanceDetail.dailyAttendance.map { daily ->
//            HistoryData(
//                date = daily.dateBs?:"",
//                day = daily.day?:"",
//                clockInTime = daily.clockInTime?:"",
//                clockOutTime = daily.clockOutTime?:"",
//                status = daily.attendanceStatus?:"",
//                isPresent = daily.attendanceStatus
//
//                )
//        }
//
//    }
//}