package com.gurkha.di

import com.gurkha.hr.data.attendanceStatus.KtorAttendanceStatusRemoteRepository
import com.gurkha.hr.data.leaveAssignee.KtorLeaveAssigneeRemoteRepository
import com.gurkha.hr.data.leaveType.KtorLeaveTypeRemoteRepository
import com.gurkha.hr.domain.attendanceStatus.repository.AttendanceStatusRemoteRepository
import com.gurkha.hr.domain.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leaveAssignee.repository.LeaveAssigneeRemoteRepository
import com.gurkha.hr.domain.leaveAssignee.usecase.LeaveAssigneeUseCase
import com.gurkha.hr.domain.leaveType.repository.LeaveTypeRemoteRepository
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

    @Factory(binds = [LeaveAssigneeRemoteRepository::class])
    fun leaveAssigneeRemoteRepository(httpClient: HttpClient) =
        KtorLeaveAssigneeRemoteRepository(httpClient)

    @Factory(binds = [LeaveTypeRemoteRepository::class])
    fun leaveTypeRemoteRepository(httpClient: HttpClient) =
        KtorLeaveTypeRemoteRepository(httpClient)

    @Factory
    fun leaveTypeUseCase(
        leaveTypeRemoteRepository: LeaveTypeRemoteRepository
    ): LeaveTypeUseCase = LeaveTypeUseCase(
        leaveTypeRemoteRepository = leaveTypeRemoteRepository
    )

    @Factory
    fun leaveAssigneeUseCase(
        leaveAssigneeRemoteRepository: LeaveAssigneeRemoteRepository
    ): LeaveAssigneeUseCase = LeaveAssigneeUseCase(
        leaveAssigneeRemoteRepository = leaveAssigneeRemoteRepository
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