package com.gurkha.hr.domain.attendance.attendanceStatus.useCase

import com.gurkha.hr.domain.attendance.attendanceStatus.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.domain.attendance.attendanceStatus.repository.AttendanceStatusRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

class AttendanceStatusUseCase(
    private val attendanceStatusRemoteRepository: AttendanceStatusRemoteRepository
) {
    suspend operator fun invoke(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<List<AttendanceStatusData>, DataError> {
        return attendanceStatusRemoteRepository.fetchAttendanceStatus(
            attendanceStatus,
            employeeName,
            isSelf
        ).map {
            it.toData()
        }
    }
}
