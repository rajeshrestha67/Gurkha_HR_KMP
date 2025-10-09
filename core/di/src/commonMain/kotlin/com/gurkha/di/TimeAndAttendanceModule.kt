package com.gurkha.di

import com.gurkha.hr.data.timeAndAttendance.KtorTimeAndAttendanceRemoteRepository
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.timeAndAttendance.repository.TimeAndAttendanceRemoteRepository
import com.gurkha.hr.domain.timeAndAttendance.usecase.TimeAndAttendanceUseCase
import com.gurkha.hr.profile.time_and_attendance.TimeAndAttendanceViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class TimeAndAttendanceModule {
    @Factory(binds = [TimeAndAttendanceRemoteRepository::class])
    fun timeAndAttendanceRemoteRepository(httpClient: HttpClient): TimeAndAttendanceRemoteRepository =
        KtorTimeAndAttendanceRemoteRepository(httpClient)

    @Factory
    fun timeAndAttendanceUseCase(timeAndAttendanceRemoteRepository: TimeAndAttendanceRemoteRepository): TimeAndAttendanceUseCase =
        TimeAndAttendanceUseCase(timeAndAttendanceRemoteRepository= timeAndAttendanceRemoteRepository)
    @KoinViewModel
    fun getTimeAndAttendanceScreenViewModel(
        timeAndAttendanceUseCase: TimeAndAttendanceUseCase
        ,requiredValidationUseCase: RequiredValidationUseCase
    ): TimeAndAttendanceViewModel = TimeAndAttendanceViewModel(
        timeAndAttendanceUseCase = timeAndAttendanceUseCase,
        requiredValidationUseCase = requiredValidationUseCase
    )
}
