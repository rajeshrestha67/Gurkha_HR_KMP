package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeRoute {
    @Serializable
    data class ViewAllRoute(val json : String, val title : String): HomeRoute

    @Serializable
    data object NotificationRoute: HomeRoute

}