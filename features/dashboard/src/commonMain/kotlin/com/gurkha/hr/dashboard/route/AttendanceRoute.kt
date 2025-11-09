package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface AttendanceRoute {
    @Serializable
    data class AttendanceRequestScreen(val date: String?, val clockStatus: String?) : AttendanceRoute
}