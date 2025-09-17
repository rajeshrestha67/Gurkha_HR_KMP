package com.gurkha.di

import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.usecase.AttendanceUseCase
import com.gurkha.hr.home.HomeScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class HomeScreenModule {

    @Factory(binds = [AttendanceRemoteRepository::class])
    fun attendanceRepository(httpClient: HttpClient) = KtorAttendanceRemoteRepository(httpClient)

    @Factory
    fun attendanceUseCase(attendanceRemoteRepository: AttendanceRemoteRepository): AttendanceUseCase =
        AttendanceUseCase(attendanceRemoteRepository)

    @KoinViewModel
    fun getHomeScreenViewModel(
        attendanceUseCase: AttendanceUseCase
    ): HomeScreenViewModel = HomeScreenViewModel(
        attendanceUseCase = attendanceUseCase
    )

}


