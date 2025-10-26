package com.gurkha.hr.profile.model.history_screen

data class HistoryScreenData(
    val date: String,
    val day: String,
    val clockInTime: String,
    val clockOutTime: String,
    val status: String,
    val isPresent: Boolean,
    val isHoliday: Boolean,
    val isAbsent: Boolean,
    val lateInTime: String,
    val earlyOutTime: String,


)