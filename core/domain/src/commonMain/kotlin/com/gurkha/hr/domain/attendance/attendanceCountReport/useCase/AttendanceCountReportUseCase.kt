package com.gurkha.hr.domain.attendance.attendanceCountReport.useCase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.attendance.attendanceCountReport.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceCountReport.model.AttendanceCountReportData
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class AttendanceCountReportUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        toDate: String,
        fromDate: String
    ): ERPResult<AttendanceCountReportData, DataError>{
        val id = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return attendanceRemoteRepository.attendanceCountReportFetch(
            employeeId = id,
            toDate = toDate,
            fromDate = fromDate
        ).map {
            it.toData()
        }
    }
}