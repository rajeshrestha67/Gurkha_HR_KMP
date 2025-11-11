package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface DashboardRoute {
    @Serializable
    data object HomeRoute : DashboardRoute

    @Serializable
    data class AttendanceRoute(val isApproved: Boolean? = null) : DashboardRoute

    @Serializable
    data object ProfileRoute : DashboardRoute

    @Serializable
    data class LeaveRoute(val isApproved: Boolean? = null) : DashboardRoute

    @Serializable
    data object NoteRoute : DashboardRoute


}