package com.gurkha.di

import com.gurkha.hr.data.attendanceStatus.KtorAttendanceStatusRemoteRepository
import com.gurkha.hr.data.leaveRequest.KtorLeaveRequestRemoteRepository
import com.gurkha.hr.domain.attendanceStatus.repository.AttendanceStatusRemoteRepository
import com.gurkha.hr.domain.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leaveAssignee.usecase.LeaveAssigneeUseCase
import com.gurkha.hr.domain.leave.repository.LeaveRemoteRepository
import com.gurkha.hr.domain.leaveType.usecase.LeaveTypeUseCase
import com.gurkha.hr.leave.leave.LeaveScreenViewModel
import com.gurkha.hr.leave.leaveRequestPage.LeaveRequestScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class LeaveScreenModule {
    @Factory(binds = [AttendanceStatusRemoteRepository::class])
    fun attendanceStatusRemoteRepository(httpClient: HttpClient) =
        KtorAttendanceStatusRemoteRepository(httpClient)

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
    fun leaveAssigneeUseCase(
        leaveRemoteRepository: LeaveRemoteRepository
    ): LeaveAssigneeUseCase = LeaveAssigneeUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )

    @Factory
    fun attendanceStatusUseCase(attendanceStatusRemoteRepository: AttendanceStatusRemoteRepository): AttendanceStatusUseCase =
        AttendanceStatusUseCase(attendanceStatusRemoteRepository)

    @KoinViewModel
    fun getLeaveScreenViewModel(
        attendanceStatusUseCase: AttendanceStatusUseCase
    ): LeaveScreenViewModel = LeaveScreenViewModel(
        attendanceStatusUseCase = attendanceStatusUseCase
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