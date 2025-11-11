package com.gurkha.hr.route

import kotlinx.serialization.Serializable


@Serializable
sealed interface AppRoute {
    @Serializable
    data object LoginRoute : AppRoute

    @Serializable
    data class DashboardRoute(
        val navigateToLeave: Boolean? = null,
        val isApproved: Boolean? = null
    ) : AppRoute

    @Serializable
    data object OnBoardingRoute : AppRoute
}