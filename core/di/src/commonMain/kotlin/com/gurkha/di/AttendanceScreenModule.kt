package com.gurkha.di

import com.gurkha.hr.attendance.AttendanceViewModel
import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class AttendanceScreenModule {
    @Factory(binds = [AttendanceRemoteRepository::class])
    fun attendanceRemoteRepository(httpClient: HttpClient) : AttendanceRemoteRepository =
        KtorAttendanceRemoteRepository(
        httpClient
    )

    @Factory
    fun attendanceStatusUseCase(
        attendanceRemoteRepository: AttendanceRemoteRepository
    ) : AttendanceStatusUseCase = AttendanceStatusUseCase(
        attendanceRemoteRepository = attendanceRemoteRepository
    )

    @KoinViewModel
    fun getAttendanceViewModel(
        attendanceStatusUseCase: AttendanceStatusUseCase
    ): AttendanceViewModel = AttendanceViewModel(
        attendanceStatusUseCase = attendanceStatusUseCase
    )
}