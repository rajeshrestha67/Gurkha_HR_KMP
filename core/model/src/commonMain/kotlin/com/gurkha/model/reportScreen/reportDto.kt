package com.gurkha.model.reportScreen

import kotlinx.serialization.Serializable

@Serializable
data class ReportSummaryResponseDTO(
    val status: String,
    val message: String,
    val detail: List<ReportSummaryDetailDTO>,
    val success: Boolean
)

@Serializable
data class ReportSummaryDetailDTO(
    val fullName: String,
    val branch: String,
    val totalDays: Int,
    val holidays: Int,
    val totalWorkingDays: Int,
    val totalLeaveTaken: Double,
    val totalPresentDays: Double,
    val totalAbsentDays: Double,
    val imageUrl: String? = null,
    val totalWorkedDays: Double
)

@Serializable
data class ReportRequestDTO(
    val branchId: String? = null,
    val bsMonth: Int?,
    val bsYear: Int?,
    val employeeId: Int?,
    val isSelf: String? = null
)
