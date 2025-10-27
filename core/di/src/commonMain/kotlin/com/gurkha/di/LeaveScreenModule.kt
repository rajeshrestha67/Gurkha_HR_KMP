package com.gurkha.di

import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.data.leaveRequest.KtorLeaveRequestRemoteRepository
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.AssigneeUseCase
import com.gurkha.hr.domain.leave.leaveReport.useCase.LeaveReportUseCase
import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.domain.leave.leaveRequest.usecase.LeaveRequestUseCase
import com.gurkha.hr.domain.leave.leaveSummary.useCase.LeaveSummaryUseCase
import com.gurkha.hr.domain.leave.leaveType.usecase.LeaveTypeUseCase
import com.gurkha.hr.leave.leave.LeaveScreenViewModel
import com.gurkha.hr.leave.leaveRequestPage.LeaveRequestScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class LeaveScreenModule {

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
    ): AssigneeUseCase = AssigneeUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )

    @Factory
    fun leaveRequestUseCase(leaveRemoteRepository: LeaveRemoteRepository): LeaveRequestUseCase =
        LeaveRequestUseCase(leaveRemoteRepository)

    @Factory
    fun leaveSummaryUseCase (
        leaveRemoteRepository: LeaveRemoteRepository
    ): LeaveSummaryUseCase = LeaveSummaryUseCase(
        leaveRemoteRepository = leaveRemoteRepository
    )

    @KoinViewModel
    fun getLeaveScreenViewModel(

        leaveReportUseCase: LeaveReportUseCase,
        leaveSummaryUseCase : LeaveSummaryUseCase,
        calendarModel: CalendarModel,
    ): LeaveScreenViewModel = LeaveScreenViewModel(
        leaveReportUseCase = leaveReportUseCase,
        leaveSummaryUseCase = leaveSummaryUseCase,
        calendarModel = calendarModel
    )

    @KoinViewModel
    fun getLeaveRequestViewModel(
        leaveRequestUseCase: LeaveRequestUseCase,
        requiredValidationUseCase: RequiredValidationUseCase,
        leaveAssigneeUseCase: AssigneeUseCase,
        leaveTypeUseCase: LeaveTypeUseCase
    ): LeaveRequestScreenViewModel =
        LeaveRequestScreenViewModel(
            leaveRequestUseCase = leaveRequestUseCase,
            requiredValidationUseCase = requiredValidationUseCase,
            leaveTypeUseCase = leaveTypeUseCase,
            assigneeUseCase = leaveAssigneeUseCase
        )
}