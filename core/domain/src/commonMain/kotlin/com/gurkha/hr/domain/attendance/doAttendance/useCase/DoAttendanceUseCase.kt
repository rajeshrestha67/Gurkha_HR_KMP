package com.gurkha.hr.domain.attendance.doAttendance.useCase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.doAttendance.mapper.toData
import com.gurkha.hr.domain.attendance.doAttendance.model.DoAttendanceData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class DoAttendanceUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(
        forDate: String,
        imageName: String

    ): ERPResult<DoAttendanceData, DataError>{
        val id = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return attendanceRemoteRepository.doAttendance(
            employeeId=id,
            forDate= forDate,
            imageName= imageName
        ).map {
            it.toData()
        }
    }
}