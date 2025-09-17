package com.gurkha.hr.domain.attendance.usecase

import com.gurkha.hr.domain.attendance.mapper.toData
import com.gurkha.hr.domain.attendance.model.AttendanceData
import com.gurkha.hr.domain.attendance.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess

class AttendanceUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository,
) {
    suspend operator fun invoke(
        fromDate: String,
        toDate: String,
        enabledManualAttendance: String,
        branchId: String ? = null
    ): ERPResult<AttendanceData, DataError> {
        return attendanceRemoteRepository.fetchAttendance(
            fromDate,
            toDate,
            enabledManualAttendance,
            branchId
        ).map {
            it.toData()
        }.onSuccess { data ->
            println("data :$data")
        }
    }
}