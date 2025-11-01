package com.gurkha.hr.domain.attendance.attendanceStatus.useCase

import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceStatus.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError


class AttendanceStatusUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository
) {
    suspend operator fun invoke(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String,
        fromDate: String,
        toDate: String
    ): ERPResult<List<AttendanceStatusData>, DataError> {
        return attendanceRemoteRepository.fetchAttendanceStatus(
            attendanceStatus,
            employeeName,
            isSelf,
            fromDate,
            toDate
        ).map {
            it.toData()
        }
    }
}
