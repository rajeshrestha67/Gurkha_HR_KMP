package com.gurkha.hr.domain.attendance.attendanceReport.usecase

import com.gurkha.hr.domain.attendance.attendanceReport.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class AttendanceUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository,
) {
    suspend operator fun invoke(
        fromDate: String,
        toDate: String,
    ): ERPResult<List<AttendanceData>, DataError> {
        return attendanceRemoteRepository.fetchAttendance(
            dateFrom = fromDate,
            toDate = toDate
        ).map {
            it.toData()
        }
    }
}