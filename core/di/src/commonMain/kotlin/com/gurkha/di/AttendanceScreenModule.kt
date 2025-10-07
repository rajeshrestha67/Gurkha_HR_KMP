package com.gurkha.di

import com.gurkha.hr.attendance.AttendanceViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class AttendanceScreenModule {
    @KoinViewModel
    fun getAttendanceViewModel(): AttendanceViewModel = AttendanceViewModel()
}