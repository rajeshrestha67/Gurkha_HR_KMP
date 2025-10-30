package com.gurkha.di

import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.profile.time_and_attendance.TimeAndAttendanceViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class TimeAndAttendanceModule {

    @KoinViewModel
    fun getTimeAndAttendanceScreenViewModel(
        timeAndAttendanceUseCase: AttendanceUseCase,
        requiredValidationUseCase: RequiredValidationUseCase,
        calendarModel: CalendarModel
    ): TimeAndAttendanceViewModel = TimeAndAttendanceViewModel(
        timeAndAttendanceUseCase = timeAndAttendanceUseCase,
        requiredValidationUseCase = requiredValidationUseCase,
        calendarModel = calendarModel
    )
}
