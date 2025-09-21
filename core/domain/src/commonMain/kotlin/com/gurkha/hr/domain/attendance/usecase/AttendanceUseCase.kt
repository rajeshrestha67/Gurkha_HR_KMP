package com.gurkha.hr.domain.attendance.usecase

import com.gurkha.hr.domain.attendance.mapper.toData
import com.gurkha.hr.domain.attendance.model.AttendanceData
import com.gurkha.hr.domain.attendance.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map

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