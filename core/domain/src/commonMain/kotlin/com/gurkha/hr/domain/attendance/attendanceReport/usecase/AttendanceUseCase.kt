package com.gurkha.hr.domain.attendance.attendanceReport.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.attendance.attendanceReport.mapper.toData
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class AttendanceUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        fromDate: String? = null,
        toDate: String? = null,
        attendanceStatus: String? = null ,
        employeeId: Int? = null
    ): ERPResult<List<AttendanceData>, DataError> {
        val id = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0

        return attendanceRemoteRepository.fetchAttendance(
            dateFrom = fromDate ,
            toDate = toDate,
            attendanceStatus = attendanceStatus,
            employeeId= id
        ).map {
            it.toData()
        }
    }
}