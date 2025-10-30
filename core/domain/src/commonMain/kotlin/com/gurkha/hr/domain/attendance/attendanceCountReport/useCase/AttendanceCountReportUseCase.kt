package com.gurkha.hr.domain.attendance.attendanceCountReport.useCase

import com.gurkha.hr.domain.attendance.attendanceCountReport.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceCountReport.model.AttendanceCountReportData
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class AttendanceCountReportUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository
) {
    suspend operator fun invoke(
        employeeId: Int,
        toDate: String,
        fromDate: String
    ): ERPResult<AttendanceCountReportData, DataError>{
        return attendanceRemoteRepository.attendanceCountReportFetch(
            employeeId = employeeId,
            toDate = toDate,
            fromDate = fromDate
        ).map {
            it.toData()
        }
    }
}