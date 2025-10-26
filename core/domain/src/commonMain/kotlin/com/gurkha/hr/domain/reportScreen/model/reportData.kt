package com.gurkha.hr.domain.reportScreen.model
data class ReportData(
    val fullName: String,
    val department: String,
    val totalDays: Int,
    val holidays: Int,
    val totalWorkingDays: Int,
    val totalLeaveTaken: Double,
    val totalPresentDays: Double,
    val totalAbsentDays: Double,
    val totalWorkedDays: Double,
    val imageUrl: String? = null

)