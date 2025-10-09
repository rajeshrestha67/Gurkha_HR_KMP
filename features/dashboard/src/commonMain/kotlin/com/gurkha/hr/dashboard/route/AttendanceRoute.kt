package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface AttendanceRoute {
    @Serializable
    data class AttendanceRequestScreen(val json: String?) : AttendanceRoute
}