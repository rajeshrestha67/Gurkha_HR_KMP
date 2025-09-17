package com.gurkha.model.attendance

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequestDto(
    val fromDate: String,
    val toDate: String,
    val enableManualAttendance: String,
    val branchId : String
)

@Serializable
data class AttendanceResponseDto(
   val status: String? = null,
    val message: String? = null,


)

@Serializable
data class AttendanceDetailResponse(
    val data : HashMap<String, AttendanceDataResponse>? = null

)
@Serializable
data class AttendanceDataResponse(
    val data : HashMap<String, Nothing>? = null

)