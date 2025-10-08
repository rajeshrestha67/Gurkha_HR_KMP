package com.gurkha.hr.domain.attendance.attendanceRequest.useCase

import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceRequest.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceRequest.model.AttendanceRequestData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class AttendanceRequestUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository
) {
    suspend operator fun invoke(
        assigneeId: Int,
        clockInTime: String,
        clockOutTime: String,
        date: String,
        remarks: String
    ): ERPResult<AttendanceRequestData, DataError> {
        return attendanceRemoteRepository.requestAttendance(
            assigneeId = assigneeId,
            clockInTime = clockInTime,
            clockOutTime = clockOutTime,
            date = date,
            remarks = remarks
        ).map {
            it.toData()
        }
    }
}