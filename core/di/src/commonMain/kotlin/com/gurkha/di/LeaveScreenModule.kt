package com.gurkha.di

import com.gurkha.hr.data.attendanceStatus.KtorAttendanceStatusRemoteRepository
import com.gurkha.hr.domain.attendanceStatus.repository.AttendanceStatusRemoteRepository
import com.gurkha.hr.domain.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.leave.LeaveScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class LeaveScreenModule {
    @Factory(binds = [AttendanceStatusRemoteRepository::class])
    fun attendanceStatusRemoteRepository(httpClient: HttpClient)=KtorAttendanceStatusRemoteRepository(httpClient)

    @Factory
    fun attendanceStatusUseCase(attendanceStatusRemoteRepository: AttendanceStatusRemoteRepository): AttendanceStatusUseCase = AttendanceStatusUseCase(attendanceStatusRemoteRepository)

    @KoinViewModel
    fun getLeaveScreenViewModel(
        attendanceStatusUseCase: AttendanceStatusUseCase
    ) : LeaveScreenViewModel = LeaveScreenViewModel(
        attendanceStatusUseCase = attendanceStatusUseCase
    )
}