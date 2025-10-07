package com.gurkha.di

import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.data.leaveRequest.KtorLeaveRequestRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.LeaveAssigneeUseCase
import com.gurkha.hr.domain.leave.leaveReport.useCase.LeaveReportUseCase
import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.domain.leave.leaveRequest.usecase.LeaveRequestUseCase
import com.gurkha.hr.domain.leave.leaveType.usecase.LeaveTypeUseCase
import com.gurkha.hr.leave.leave.LeaveScreenViewModel
import com.gurkha.hr.leave.leaveRequestPage.LeaveRequestScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class LeaveScreenModule {
    @Factory(binds = [AttendanceRemoteRepository::class])
    fun attendanceRemoteRepository(httpClient: HttpClient) =
        KtorAttendanceRemoteRepository(httpClient)

    @Factory(binds = [LeaveRemoteRepository::class])
    fun leaveRemoteRepository(httpClient: HttpClient) =
        KtorLeaveRequestRemoteRepository(httpClient)

    @Factory
    fun leaveTypeUseCase(
        leaveRemoteRepository: LeaveRemoteRepository
    ): LeaveTypeUseCase = LeaveTypeUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )

    @Factory
    fun leaveReportUseCase(
        leaveRemoteRepository: LeaveRemoteRepository
    ): LeaveReportUseCase = LeaveReportUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )


    @Factory
    fun leaveAssigneeUseCase(
        leaveRemoteRepository: LeaveRemoteRepository
    ): LeaveAssigneeUseCase = LeaveAssigneeUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )

    @Factory
    fun attendanceStatusUseCase(attendanceRemoteRepository: AttendanceRemoteRepository): AttendanceStatusUseCase =
        AttendanceStatusUseCase(attendanceRemoteRepository = attendanceRemoteRepository)

    @Factory
    fun leaveRequestUseCase(leaveRemoteRepository: LeaveRemoteRepository): LeaveRequestUseCase =
        LeaveRequestUseCase(leaveRemoteRepository)

    @KoinViewModel
    fun getLeaveScreenViewModel(
        leaveRequestUseCase: LeaveRequestUseCase,
        leaveReportUseCase: LeaveReportUseCase
    ): LeaveScreenViewModel = LeaveScreenViewModel(
        leaveRequestUseCase = leaveRequestUseCase,
        leaveReportUseCase = leaveReportUseCase
    )

    @KoinViewModel
    fun getLeaveRequestViewModel(
        requiredValidationUseCase: RequiredValidationUseCase,
        leaveAssigneeUseCase: LeaveAssigneeUseCase,
        leaveTypeUseCase: LeaveTypeUseCase
    ): LeaveRequestScreenViewModel =
        LeaveRequestScreenViewModel(
            requiredValidationUseCase = requiredValidationUseCase,
            leaveTypeUseCase = leaveTypeUseCase,
            leaveAssigneeUseCase = leaveAssigneeUseCase
        )
}