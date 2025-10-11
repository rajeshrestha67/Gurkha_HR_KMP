package com.gurkha.di

import com.gurkha.hr.attendance.AttendanceViewModel
import com.gurkha.hr.attendanceRequestScreen.AttendanceRequestViewModel
import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.data.leaveRequest.KtorLeaveRequestRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceRequest.useCase.AttendanceRequestUseCase
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.AssigneeUseCase
import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class AttendanceScreenModule {
    @Factory(binds = [AttendanceRemoteRepository::class])
    fun attendanceRemoteRepository(httpClient: HttpClient) =
        KtorAttendanceRemoteRepository(httpClient)

    @Factory
    fun attendanceStatusUseCase(attendanceRemoteRepository: AttendanceRemoteRepository): AttendanceStatusUseCase =
        AttendanceStatusUseCase(attendanceRemoteRepository = attendanceRemoteRepository)

    @Factory(binds = [LeaveRemoteRepository::class])
    fun leaveRemoteRepository(httpClient: HttpClient): LeaveRemoteRepository =
        KtorLeaveRequestRemoteRepository(httpClient)


    @Factory
    fun assigneeUseCase(
        leaveRemoteRepository: LeaveRemoteRepository
    ): AssigneeUseCase = AssigneeUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )

    @Factory
    fun attendanceRequestUseCase(
        attendanceRemoteRepository : AttendanceRemoteRepository
    ): AttendanceRequestUseCase = AttendanceRequestUseCase(
        attendanceRemoteRepository = attendanceRemoteRepository
    )

    @KoinViewModel
    fun getAttendanceViewModel(
        attendanceStatusUseCase: AttendanceStatusUseCase,
    ): AttendanceViewModel = AttendanceViewModel(
        attendanceStatusUseCase = attendanceStatusUseCase,
    )

    @KoinViewModel
    fun getAttendanceRequestScreenViewModel(
        requiredValidationUseCase: RequiredValidationUseCase,
        attendanceRequestUseCase: AttendanceRequestUseCase,
        assigneeUseCase: AssigneeUseCase
    ): AttendanceRequestViewModel = AttendanceRequestViewModel(
        assigneeUseCase = assigneeUseCase,
        attendanceRequestUseCase = attendanceRequestUseCase,
        requiredValidationUseCase = requiredValidationUseCase
    )
}