package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface LeaveRoute {
    @Serializable
    data class LeaveRequestPageRoute(val json: String?) : LeaveRoute
}