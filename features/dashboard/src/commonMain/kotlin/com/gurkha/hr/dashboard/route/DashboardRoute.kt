package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface DashboardRoute {
    @Serializable
    data object HomeRoute : DashboardRoute

    @Serializable
    data object AttendanceRoute : DashboardRoute

    @Serializable
    data object ProfileRoute : DashboardRoute

    @Serializable
    data object LeaveRoute : DashboardRoute

    @Serializable
    data object NoteRoute : DashboardRoute


}