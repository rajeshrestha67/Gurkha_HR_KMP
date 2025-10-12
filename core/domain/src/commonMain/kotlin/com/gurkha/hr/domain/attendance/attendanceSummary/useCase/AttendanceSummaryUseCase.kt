package com.gurkha.hr.domain.attendance.attendanceSummary.useCase

import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceSummary.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceSummary.model.AttendanceSummaryData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class AttendanceSummaryUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository
) {
    suspend operator fun invoke(): ERPResult<AttendanceSummaryData, DataError> {
        return attendanceRemoteRepository.fetchAttendanceSummary().map {
            it.toData()
        }
    }
}