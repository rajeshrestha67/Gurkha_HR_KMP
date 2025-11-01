package com.gurkha.hr.domain.attendance.doAttendance.useCase

import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.doAttendance.mapper.toData
import com.gurkha.hr.domain.attendance.doAttendance.model.DoAttendanceData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class DoAttendanceUseCase(
    private val attendanceRemoteRepository: AttendanceRemoteRepository
) {
    suspend operator fun invoke(
        employeeId:Int,
        forDate: String,
        imageName: String

    ): ERPResult<DoAttendanceData, DataError>{
        return attendanceRemoteRepository.doAttendance(
            employeeId=employeeId,
            forDate= forDate,
            imageName= imageName
        ).map {
            it.toData()
        }
    }
}