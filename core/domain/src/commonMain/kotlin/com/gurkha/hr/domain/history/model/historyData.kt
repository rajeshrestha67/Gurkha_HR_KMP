package com.gurkha.hr.domain.history.model

data class HistoryData(
    val date: String,
    val day: String,
    val clockInTime: String,
    val clockOutTime: String,
    val status: String,
    val isPresent: Boolean,
    val isHoliday: Boolean,
    val isAbsent: Boolean,
)